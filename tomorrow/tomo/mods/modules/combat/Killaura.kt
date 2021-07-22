/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.combat

import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import tomorrow.tomo.utils.cheats.world.TimerUtil
import tomorrow.tomo.mods.modules.combat.Killaura.MarkMods
import tomorrow.tomo.mods.modules.combat.Killaura.AuraMode
import java.util.function.ToDoubleFunction
import tomorrow.tomo.utils.math.MathUtil
import java.util.stream.Collectors
import org.lwjgl.opengl.GL11
import tomorrow.tomo.Client
import tomorrow.tomo.event.EventHandler
import tomorrow.tomo.event.events.rendering.EventRender3D
import tomorrow.tomo.event.events.world.EventPacketSend
import tomorrow.tomo.event.events.world.EventPostUpdate
import tomorrow.tomo.event.events.world.EventPreUpdate
import tomorrow.tomo.event.value.Mode
import tomorrow.tomo.event.value.Numbers
import tomorrow.tomo.event.value.Option
import tomorrow.tomo.luneautoleak.othercheck.ReVerify
import tomorrow.tomo.managers.FriendManager
import tomorrow.tomo.managers.ModuleManager
import tomorrow.tomo.mods.Module
import tomorrow.tomo.mods.ModuleType
import tomorrow.tomo.mods.modules.player.Teams
import tomorrow.tomo.utils.math.RotationUtil
import tomorrow.tomo.utils.render.RenderUtil
import java.awt.Color
import java.util.ArrayList
import java.util.Comparator

class Killaura : Module("KillAura", "Auto attack", ModuleType.Combat) {
    private val timer = TimerUtil()
    private var targets: MutableList<*> = ArrayList<Any?>(0)
    private var index = 0
    private val aps = Numbers<Number>("APS", "APS", 10.0, 1.0, 20.0, 0.5)
    private val blocking = Option<Boolean>("Autoblock", "autoblock", true)
    private val players = Option<Boolean>("Players", "players", true)
    private val animals = Option<Boolean>("Animals", "animals", true)
    private val mobs = Option<Boolean>("Mobs", "mobs", false)
    private val invis = Option<Boolean>("Invisibles", "invisibles", false)
    private val markMode: Mode<Enum<*>?> = Mode<Enum<*>?>("MarkMode", "Markmode", MarkMods.values(), MarkMods.HeadBox)
    private val mark = Option<Boolean>("Mark", "Mark", true)
    private val mode: Mode<Enum<*>?> =
        Mode<Enum<*>?>("Mode", "mode", AuraMode.values() as Array<Enum<*>?>, AuraMode.Switch as Enum<*>)
    private var isBlocking = false
    private val angleComparator: Comparator<Entity?> = Comparator.comparingDouble { e2: Entity? ->
        RotationUtil.getRotations(e2)[0]
            .toDouble()
    }

    internal enum class MarkMods {
        HeadBox, Box
    }

    override fun onDisable() {
        targets.clear()
        target = null
        if (canBlock() && isBlocking) {
            stopAutoBlock()
        }
        //        if (this.blocking.getValue().booleanValue() && this.canBlock() && this.mc.thePlayer.isBlocking()) {
//            this.stopAutoBlockHypixel();
//        }
    }

    override fun onEnable() {
        target = null
        index = 0
    }

    private fun canBlock(): Boolean {
        return if (mc.thePlayer.currentEquippedItem != null
            && mc.thePlayer.inventory.getCurrentItem().item is ItemSword
        ) {
            true
        } else false
    }

    //    private void startAutoBlockHypixel() {
    //        if (Helper.onServer("hypixel")) {
    //            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
    //            if (this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem())) {
    //                this.mc.getItemRenderer().resetEquippedProgress2();
    //            }
    //        }
    //    }
    //
    //    private void stopAutoBlockHypixel() {
    //        if (Helper.onServer("hypixel")) {
    //            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
    //            this.mc.playerController.onStoppedUsingItem(this.mc.thePlayer);
    //        }
    //    }
    private fun startAutoBlock() {
        mc.playerController.sendUseItem(
            mc.thePlayer, mc.theWorld,
            mc.thePlayer.currentEquippedItem
        )
        //		mc.gameSettings.keyBindUseItem.pressed = true;
        isBlocking = true
    }

    private fun stopAutoBlock() {
        mc.thePlayer.sendQueue.addToSendQueue(
            C07PacketPlayerDigging(
                C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN
            )
        )
        //		mc.gameSettings.keyBindUseItem.pressed = false;
        isBlocking = false
    }

    private fun shouldAttack(): Boolean {
        return timer.hasReached(1000.0 / (aps.getValue().toFloat() + MathUtil.randomDouble(0.0, 5.0)))
    }

    @EventHandler
    private fun onUpdate(event: EventPreUpdate) {
        setSuffix(mode.getValue())
        targets = loadTargets()
//        targets.sortedWith(angleComparator)
        if (target != null && target is EntityPlayer || target is EntityMob
            || target is EntityAnimal
        ) {
            target = null
        }
        if (mc.thePlayer.ticksExisted % 50 == 0 && targets.size > 1) {
            ++index
        }
        if (!targets.isEmpty()) {
            if (index >= targets.size) {
                index = 0
            }
            target = targets[index] as EntityLivingBase
            event.yaw = RotationUtil.faceTarget(target, 1000.0f, 1000.0f, false)[0]
            if (!AutoHeal.currentlyPotting) {
                event.pitch = RotationUtil.faceTarget(target, 1000.0f, 1000.0f, false)[1]
            }
            if (blocking.getValue()!! && canBlock() && isBlocking) {
                stopAutoBlock()
            }
        }
        if (target == null && canBlock() && isBlocking) {
            stopAutoBlock()
        }
    }

    private fun swap(slot: Int, hotbarNum: Int) {
        mc.playerController.windowClick(
            mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
            mc.thePlayer
        )
    }

    @EventHandler
    private fun onUpdatePost(e: EventPostUpdate) {
        if (target != null) {
            val angle = Math.toRadians((target!!.rotationYaw - 90.0f + 360.0f).toDouble()) % 360.0
            if (shouldAttack()) {
                if (mode.getValue() === AuraMode.Switch) {
                    attack()
                } else {
                    swap(9, mc.thePlayer.inventory.currentItem)
                    attack()
                }
                timer.reset()
            }
            if (canBlock() && blocking.getValue() && !mc.thePlayer.isBlocking) {
                startAutoBlock()
            }
        }
    }

    private fun loadTargets(): MutableList<Entity> {
        return mc.theWorld.loadedEntityList.stream()
            .filter { e: Entity ->
                (mc.thePlayer.getDistanceToEntity(e)
                    .toDouble() <= reach?.value!!.toFloat()
                        && qualifies(e))
            }
            .collect(Collectors.toList())
    }

    private fun qualifies(e: Entity): Boolean {
        if (e === mc.thePlayer) {
            return false
        }
        val ab = ModuleManager.getModuleByClass(
            AntiBots::class.java
        ) as AntiBots
        if (ab.isServerBot(e) && ab.isEnabled) {
            return false
        }
        if (!e.isEntityAlive) {
            return false
        }
        if (FriendManager.isFriend(e.name)) {
            return false
        }
        if (e is EntityPlayer && players.getValue()
            && (!Teams.isOnSameTeam(e) || !ab.isServerBot(e) && ab.mode.getValue() === AntiBots.mods.CubeCraft)
        ) {
            return true
        }
        if (e is EntityMob && mobs.getValue()) {
            return true
        }
        if (e is EntityAnimal && animals.getValue()) {
            return true
        }
        if (e is EntityVillager && animals.getValue()) return true
        return if (e.isInvisible && !invis.getValue()) {
            false
        } else false
    }

    private fun attack() {
        mc.thePlayer.swingItem()
        mc.thePlayer.sendQueue
            .addToSendQueue(C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK))
    }

    @EventHandler
    private fun blockinglistener(packet: EventPacketSend) {
//        C07PacketPlayerDigging packetPlayerDigging;
//        C08PacketPlayerBlockPlacement blockPlacement;
//        if (packet.getPacket() instanceof C07PacketPlayerDigging
//                && (packetPlayerDigging = (C07PacketPlayerDigging) packet.getPacket()).getStatus()
//                .equals((Object) C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)) {
//            this.isBlocking = false;
//        }
//        if (packet.getPacket() instanceof C08PacketPlayerBlockPlacement
//                && (blockPlacement = (C08PacketPlayerBlockPlacement) packet.getPacket()).getStack() != null
//                && blockPlacement.getStack().getItem() instanceof ItemSword
//                && blockPlacement.getPosition().equals(new BlockPos(-1, -1, -1))) {
//            this.isBlocking = true;
//        }
    }

    @EventHandler
    fun onRender3D(e: EventRender3D?) {
        if (target == null) {
            return
        }
        var color = Color(0, 136, 255)
        if (target!!.hurtTime > 0) {
            color = Color(255, 0, 0)
        }
        when (markMode.getValue()!!.name) {
            "HeadBox" -> {
                var x = (target!!.lastTickPosX
                        + (target!!.posX - target!!.lastTickPosX) * mc.timer.renderPartialTicks
                        - RenderManager.renderPosX)
                var y = (target!!.lastTickPosY
                        + (target!!.posY - target!!.lastTickPosY) * mc.timer.renderPartialTicks
                        - RenderManager.renderPosY)
                var z = (target!!.lastTickPosZ
                        + (target!!.posZ - target!!.lastTickPosZ) * mc.timer.renderPartialTicks
                        - RenderManager.renderPosZ)
                if (target is EntityPlayer) {
                    x -= 0.275
                    z -= 0.275
                    y += target?.getEyeHeight()!! - 0.225 - if (target?.isSneaking() == true) 0.25 else 0.0
                    val mid = 0.275
                    GL11.glPushMatrix()
                    GL11.glEnable(3042)
                    GL11.glBlendFunc(770, 771)
                    val rotAdd = -0.25 * (target?.rotationPitch?.let { Math.abs(it) }!! / 90.0f)
                    GL11.glTranslated(0.0, rotAdd, 0.0)
                    GL11.glTranslated(x + mid, y + mid, z + mid)
                    GL11.glRotated((-target!!?.rotationYaw % 360.0f).toDouble(), 0.0, 1.0, 0.0)
                    GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid))
                    GL11.glTranslated(x + mid, y + mid, z + mid)
                    GL11.glRotated(target?.rotationPitch!!.toDouble(), 1.0, 0.0, 0.0)
                    GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid))
                    GL11.glDisable(3553)
                    GL11.glEnable(2848)
                    GL11.glDisable(2929)
                    GL11.glDepthMask(false)
                    GL11.glColor4f(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, 1f)
                    GL11.glLineWidth(1.0f)
                    RenderUtil.drawOutlinedBoundingBox(
                        AxisAlignedBB(
                            x - 0.0025, y - 0.0025, z - 0.0025,
                            x + 0.55 + 0.0025, y + 0.55 + 0.0025, z + 0.55 + 0.0025
                        )
                    )
                    GL11.glColor4f(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, 0.3f)
                    RenderUtil.drawBoundingBox(
                        AxisAlignedBB(
                            x - 0.0025, y - 0.0025, z - 0.0025, x + 0.55 + 0.0025,
                            y + 0.55 + 0.0025, z + 0.55 + 0.0025
                        )
                    )
                    GL11.glDisable(2848)
                    GL11.glEnable(3553)
                    GL11.glEnable(2929)
                    GL11.glDepthMask(true)
                    GL11.glDisable(3042)
                    GL11.glPopMatrix()
                } else {
                    val width = (target!!.entityBoundingBox.maxX
                            - target!!.entityBoundingBox.minX)
                    val height = target!!.entityBoundingBox.maxY
                    -target!!.entityBoundingBox.minY + 0.25
                    val red = 0.0f
                    val green = 0.5f
                    val blue = 1.0f
                    val alpha = 0.5f
                    val lineRed = 0.0f
                    val lineGreen = 0.5f
                    val lineBlue = 1.0f
                    val lineAlpha = 1.0f
                    val lineWdith = 2.0f
                    RenderUtil.drawEntityESP(
                        x, y, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue,
                        lineAlpha, lineWdith
                    )
                }
            }
            "Box" -> {
                mc.renderManager
                var x = (target!!.lastTickPosX
                        + (target!!.posX - target!!.lastTickPosX) * mc.timer.renderPartialTicks
                        - RenderManager.renderPosX)
                mc.renderManager
                var y = (target!!.lastTickPosY
                        + (target!!.posY - target!!.lastTickPosY) * mc.timer.renderPartialTicks
                        - RenderManager.renderPosY)
                mc.renderManager
                var z = (target!!.lastTickPosZ
                        + (target!!.posZ - target!!.lastTickPosZ) * mc.timer.renderPartialTicks
                        - RenderManager.renderPosZ)
                if (target is EntityPlayer) {
                    x -= 0.5
                    z -= 0.5
                    y += target?.getEyeHeight()!! + 0.35 - if (target!!?.isSneaking()) 0.25 else 0.0
                    val mid = 0.5
                    GL11.glPushMatrix()
                    GL11.glEnable(3042)
                    GL11.glBlendFunc(770, 771)
                    val rotAdd = -0.25 * (Math.abs(target!!?.rotationPitch) / 90.0f)
                    GL11.glTranslated(x + mid, y + mid, z + mid)
                    GL11.glRotated((-target!!?.rotationYaw % 360.0f).toDouble(), 0.0, 1.0, 0.0)
                    GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid))
                    GL11.glDisable(3553)
                    GL11.glEnable(2848)
                    GL11.glDisable(2929)
                    GL11.glDepthMask(false)
                    GL11.glColor4f(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, 1.0f)
                    GL11.glLineWidth(2.0f)
                    RenderUtil.drawOutlinedBoundingBox(AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0))
                    GL11.glColor4f(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, 0.5f)
                    RenderUtil.drawBoundingBox(AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0))
                    GL11.glDisable(2848)
                    GL11.glEnable(3553)
                    GL11.glEnable(2929)
                    GL11.glDepthMask(true)
                    GL11.glDisable(3042)
                    GL11.glPopMatrix()
                } else {
                    val width = (target!!.entityBoundingBox.maxZ
                            - target!!.entityBoundingBox.minZ)
                    val height = 0.1
                    val red = 0.0f
                    val green = 0.5f
                    val blue = 1.0f
                    val alpha = 0.5f
                    val lineRed = 0.0f
                    val lineGreen = 0.5f
                    val lineBlue = 1.0f
                    val lineAlpha = 1.0f
                    val lineWdith = 2.0f
                    RenderUtil.drawEntityESP(
                        x, y + target!!.eyeHeight + 0.25, z, width, height, red, green,
                        blue, alpha, lineRed, lineGreen, lineBlue, lineAlpha, lineWdith
                    )
                }
            }
            else -> {
            }
        }
    }

    internal enum class AuraMode {
        Switch, Tick
    }

    companion object {
        @JvmField
        var target: EntityLivingBase? = null
        @JvmField
        var reach: Numbers<Number>? = Numbers("Reach", "reach", 4.5, 1.0, 6.0, 0.1)
        @JvmField
        var md5flag2 = 4
    }

    init {
        ReVerify()
        addValues(
            aps, reach, blocking, mark, players, animals, mobs,
            invis, mode, markMode
        )
        if (Client.md5flag != 0) {
            reach = null
        }
        if (md5flag2 != 0) {
            reach = null
        }
    }
}