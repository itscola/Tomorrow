/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.combat;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import tomorrow.tomo.Client;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.rendering.EventRender3D;
import tomorrow.tomo.event.events.world.EventPacketSend;
import tomorrow.tomo.event.events.world.EventPostUpdate;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.managers.FriendManager;
import tomorrow.tomo.mods.Mod;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.mods.modules.movement.Speed;
import tomorrow.tomo.mods.modules.player.Teams;
import tomorrow.tomo.utils.cheats.player.Helper;
import tomorrow.tomo.utils.cheats.world.TimerUtil;
import tomorrow.tomo.utils.math.MathUtil;
import tomorrow.tomo.utils.math.RotationUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import tomorrow.tomo.utils.render.RenderUtil;

@Mod(name = "KillAura", description = "Auto hurt entities.", type = ModuleType.Combat)
public class Killaura extends Module {
	private TimerUtil timer = new TimerUtil();
	public static EntityLivingBase target;
	private List targets = new ArrayList(0);
	private int index;
	private Numbers<Double> aps = new Numbers<Double>("APS", "APS", 10.0, 1.0, 20.0, 0.5);
	private Numbers<Double> reach = new Numbers<Double>("Reach", "reach", 4.5, 1.0, 6.0, 0.1);
	private Option<Boolean> blocking = new Option<Boolean>("Autoblock", "autoblock", true);
	private Option<Boolean> players = new Option<Boolean>("Players", "players", true);
	private Option<Boolean> animals = new Option<Boolean>("Animals", "animals", true);
	private Option<Boolean> mobs = new Option<Boolean>("Mobs", "mobs", false);
	private Option<Boolean> invis = new Option<Boolean>("Invisibles", "invisibles", false);
	private Mode<Enum> markMode = new Mode("MarkMode", "Markmode", MarkMods.values(), MarkMods.HeadBox);
	private Option<Boolean> mark = new Option<Boolean>("Mark", "Mark", true);
	private Mode<Enum> mode = new Mode("Mode", "mode", (Enum[]) AuraMode.values(), (Enum) AuraMode.Switch);

	private boolean isBlocking;
	private Comparator<Entity> angleComparator = Comparator.comparingDouble(e2 -> RotationUtil.getRotations(e2)[0]);

	public Killaura() {
		super("KillAura", "Auto attack", ModuleType.Combat);
		this.addValues(this.aps, this.reach, this.blocking, this.mark, this.players, this.animals, this.mobs,
				this.invis, this.mode,this.markMode);
	}

	enum MarkMods {
		HeadBox, Box
	}

	@Override
	public void onDisable() {
		this.targets.clear();
//        if (this.blocking.getValue().booleanValue() && this.canBlock() && this.mc.thePlayer.isBlocking()) {
//            this.stopAutoBlockHypixel();
//        }
	}

	@Override
	public void onEnable() {
		this.target = null;
		this.index = 0;
	}

	private boolean canBlock() {
		if (this.mc.thePlayer.getCurrentEquippedItem() != null
				&& this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
			return true;
		}
		return false;
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

	private void startAutoBlock() {
		this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld,
				this.mc.thePlayer.getCurrentEquippedItem());
	}

	private void stopAutoBlock() {
		this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
				C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
	}

	private boolean shouldAttack() {
		return this.timer.hasReached(1000.0 / (this.aps.getValue() + MathUtil.randomDouble(0.0, 5.0)));
	}

	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		this.setSuffix(this.mode.getValue());
		this.targets = this.loadTargets();
		this.targets.sort(this.angleComparator);
		if (this.target != null && this.target instanceof EntityPlayer || this.target instanceof EntityMob
				|| this.target instanceof EntityAnimal) {
			this.target = null;
		}
		if (this.mc.thePlayer.ticksExisted % 50 == 0 && this.targets.size() > 1) {
			++this.index;
		}
		if (!this.targets.isEmpty()) {
			if (this.index >= this.targets.size()) {
				this.index = 0;
			}
			this.target = (EntityLivingBase) this.targets.get(this.index);
			event.setYaw(RotationUtil.faceTarget(this.target, 1000.0f, 1000.0f, false)[0]);
			mc.thePlayer.rotationYawHead = RotationUtil.faceTarget(this.target, 1000.0f, 1000.0f, false)[0];
			if (!AutoHeal.currentlyPotting) {
				event.setPitch(RotationUtil.faceTarget(this.target, 1000.0f, 1000.0f, false)[1]);
			}

			if (this.blocking.getValue().booleanValue() && this.canBlock() && this.isBlocking) {
				this.stopAutoBlock();

			}
		}
	}

	private void swap(int slot, int hotbarNum) {
		this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
				this.mc.thePlayer);
	}

	@EventHandler
	private void onUpdatePost(EventPostUpdate e) {
		if (this.target != null) {
			double angle = Math.toRadians(this.target.rotationYaw - 90.0f + 360.0f) % 360.0;
			if (this.shouldAttack()) {
				if (this.mode.getValue() == AuraMode.Switch) {
					this.attack();
				} else {
					this.swap(9, this.mc.thePlayer.inventory.currentItem);
					this.attack();
				}
				this.timer.reset();
			}
			if (this.canBlock() && this.blocking.getValue().booleanValue() && !this.mc.thePlayer.isBlocking()) {
				this.startAutoBlock();
			}
		}
	}

	private List<Entity> loadTargets() {
		return this.mc.theWorld.loadedEntityList.stream()
				.filter(e -> (double) this.mc.thePlayer.getDistanceToEntity((Entity) e) <= this.reach.getValue()
						&& this.qualifies((Entity) e))
				.collect(Collectors.toList());
	}

	private boolean qualifies(Entity e) {
		if (e == this.mc.thePlayer) {
			return false;
		}
		AntiBots ab = (AntiBots) Client.instance.getModuleManager().getModuleByClass(AntiBots.class);
		if (ab.isServerBot(e) && ab.isEnabled()) {
			return false;
		}
		if (!e.isEntityAlive()) {
			return false;
		}

		if (FriendManager.isFriend(e.getName())) {
			return false;
		}
		if (e instanceof EntityPlayer && this.players.getValue()
				&& (!Teams.isOnSameTeam(e) || (!ab.isServerBot(e) && ab.mode.getValue() == AntiBots.mods.CubeCraft))) {
			return true;
		}
		if (e instanceof EntityMob && this.mobs.getValue()) {
			return true;
		}
		if (e instanceof EntityAnimal && this.animals.getValue()) {
			return true;
		}
		if (e instanceof EntityVillager && animals.getValue())
			return true;

		if (e.isInvisible() && !this.invis.getValue()) {
			return false;
		}
		return false;
	}

	private void attack() {
		this.mc.thePlayer.swingItem();
		this.mc.thePlayer.sendQueue
				.addToSendQueue(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));
	}

	@EventHandler
	private void blockinglistener(EventPacketSend packet) {
		C07PacketPlayerDigging packetPlayerDigging;
		C08PacketPlayerBlockPlacement blockPlacement;
		if (packet.getPacket() instanceof C07PacketPlayerDigging
				&& (packetPlayerDigging = (C07PacketPlayerDigging) packet.getPacket()).getStatus()
						.equals((Object) C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)) {
			this.isBlocking = false;
		}
		if (packet.getPacket() instanceof C08PacketPlayerBlockPlacement
				&& (blockPlacement = (C08PacketPlayerBlockPlacement) packet.getPacket()).getStack() != null
				&& blockPlacement.getStack().getItem() instanceof ItemSword
				&& blockPlacement.getPosition().equals(new BlockPos(-1, -1, -1))) {
			this.isBlocking = true;
		}
	}

	@EventHandler
	public void onRender3D(EventRender3D e) {
		if (Killaura.target == null) {
			return;
		}
		Color color = new Color(0, 136, 255);
		if (Killaura.target.hurtTime > 0) {
			color = new Color(255, 0, 0);
		}
		switch (markMode.getValue().name()) {
		case "HeadBox": {
			double x = Killaura.target.lastTickPosX
					+ (Killaura.target.posX - Killaura.target.lastTickPosX) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosX;
			double y = Killaura.target.lastTickPosY
					+ (Killaura.target.posY - Killaura.target.lastTickPosY) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosY;
			double z = Killaura.target.lastTickPosZ
					+ (Killaura.target.posZ - Killaura.target.lastTickPosZ) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosZ;
			if (Killaura.target instanceof EntityPlayer) {
				x -= 0.275;
				z -= 0.275;
				y += Killaura.target.getEyeHeight() - 0.225 - (Killaura.target.isSneaking() ? 0.25 : 0.0);
				final double mid = 0.275;
				GL11.glPushMatrix();
				GL11.glEnable(3042);
				GL11.glBlendFunc(770, 771);
				final double rotAdd = -0.25 * (Math.abs(Killaura.target.rotationPitch) / 90.0f);
				GL11.glTranslated(0.0, rotAdd, 0.0);
				GL11.glTranslated(x + mid, y + mid, z + mid);
				GL11.glRotated((double) (-Killaura.target.rotationYaw % 360.0f), 0.0, 1.0, 0.0);
				GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid));
				GL11.glTranslated(x + mid, y + mid, z + mid);
				GL11.glRotated((double) Killaura.target.rotationPitch, 1.0, 0.0, 0.0);
				GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid));
				GL11.glDisable(3553);
				GL11.glEnable(2848);
				GL11.glDisable(2929);
				GL11.glDepthMask(false);
				GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1f);
				GL11.glLineWidth(1.0f);
				RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - 0.0025, y - 0.0025, z - 0.0025,
						x + 0.55 + 0.0025, y + 0.55 + 0.0025, z + 0.55 + 0.0025));
				GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.3f);
				RenderUtil.drawBoundingBox(new AxisAlignedBB(x - 0.0025, y - 0.0025, z - 0.0025, x + 0.55 + 0.0025,
						y + 0.55 + 0.0025, z + 0.55 + 0.0025));
				GL11.glDisable(2848);
				GL11.glEnable(3553);
				GL11.glEnable(2929);
				GL11.glDepthMask(true);
				GL11.glDisable(3042);
				GL11.glPopMatrix();
			} else {
				final double width = Killaura.target.getEntityBoundingBox().maxX
						- Killaura.target.getEntityBoundingBox().minX;
				final double height = Killaura.target.getEntityBoundingBox().maxY
						- Killaura.target.getEntityBoundingBox().minY + 0.25;
				final float red = 0.0f;
				final float green = 0.5f;
				final float blue = 1.0f;
				final float alpha = 0.5f;
				final float lineRed = 0.0f;
				final float lineGreen = 0.5f;
				final float lineBlue = 1.0f;
				final float lineAlpha = 1.0f;
				final float lineWdith = 2.0f;
				RenderUtil.drawEntityESP(x, y, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue,
						lineAlpha, lineWdith);
			}
			break;
		}
		case "Box": {
			this.mc.getRenderManager();
			double x = Killaura.target.lastTickPosX
					+ (Killaura.target.posX - Killaura.target.lastTickPosX) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosX;
			this.mc.getRenderManager();
			double y = Killaura.target.lastTickPosY
					+ (Killaura.target.posY - Killaura.target.lastTickPosY) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosY;
			this.mc.getRenderManager();
			double z = Killaura.target.lastTickPosZ
					+ (Killaura.target.posZ - Killaura.target.lastTickPosZ) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosZ;
			if (Killaura.target instanceof EntityPlayer) {
				x -= 0.5;
				z -= 0.5;
				y += Killaura.target.getEyeHeight() + 0.35 - (Killaura.target.isSneaking() ? 0.25 : 0.0);
				final double mid = 0.5;
				GL11.glPushMatrix();
				GL11.glEnable(3042);
				GL11.glBlendFunc(770, 771);
				final double rotAdd = -0.25 * (Math.abs(Killaura.target.rotationPitch) / 90.0f);
				GL11.glTranslated(x + mid, y + mid, z + mid);
				GL11.glRotated((double) (-Killaura.target.rotationYaw % 360.0f), 0.0, 1.0, 0.0);
				GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid));
				GL11.glDisable(3553);
				GL11.glEnable(2848);
				GL11.glDisable(2929);
				GL11.glDepthMask(false);
				GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
				GL11.glLineWidth(2.0f);
				RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
				GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
				RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
				GL11.glDisable(2848);
				GL11.glEnable(3553);
				GL11.glEnable(2929);
				GL11.glDepthMask(true);
				GL11.glDisable(3042);
				GL11.glPopMatrix();
			} else {
				final double width = Killaura.target.getEntityBoundingBox().maxZ
						- Killaura.target.getEntityBoundingBox().minZ;
				final double height = 0.1;
				final float red = 0.0f;
				final float green = 0.5f;
				final float blue = 1.0f;
				final float alpha = 0.5f;
				final float lineRed = 0.0f;
				final float lineGreen = 0.5f;
				final float lineBlue = 1.0f;
				final float lineAlpha = 1.0f;
				final float lineWdith = 2.0f;
				RenderUtil.drawEntityESP(x, y + Killaura.target.getEyeHeight() + 0.25, z, width, height, red, green,
						blue, alpha, lineRed, lineGreen, lineBlue, lineAlpha, lineWdith);
			}
			break;
		}
		default: {
			break;
		}
		}

	}

	static enum AuraMode {
		Switch, Tick;
	}

}
