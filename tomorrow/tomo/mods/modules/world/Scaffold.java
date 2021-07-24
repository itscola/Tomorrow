package tomorrow.tomo.mods.modules.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import tomorrow.tomo.Client;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.rendering.EventRender2D;
import tomorrow.tomo.event.events.world.EventMove;
import tomorrow.tomo.event.events.world.EventPacketSend;
import tomorrow.tomo.event.events.world.EventPostUpdate;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.luneautoleak.othercheck.ReVerify;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.mods.modules.movement.Speed;
import tomorrow.tomo.utils.cheats.player.PlayerUtils;
import tomorrow.tomo.utils.cheats.world.TimerUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Scaffold extends Module {
    public Mode mode = new Mode("Mode", "Mode", new String[]{"Normal", "AAC", "Hypixel"}, "Hypixel");
    public final Option<Boolean> swing = new Option<>("Swing", true);
    public Option<Boolean> tower = new Option<>("Tower", true);
    public Option<Boolean> towerMove = new Option<>("TowerMove", true);
    public Option<Boolean> silent = new Option<>("AutoBlock", true);
    public Option<Boolean> down = new Option<>("DownScaffold", true);
    private final Option<Boolean> mark = new Option<>("Mark", true);
    private float width;
    private double y;
    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil autoBlockCoolDown = new TimerUtil();
    private Block block;
    private BlockData blockData;
    private int slot;
    private float lastYaw, lastPitch;
    private float blockPitch;
    private float blockYaw;
    private int towerTick;
    private boolean b;
    public boolean shouldDown;
    public boolean shouldSafeWalk;
    public final static List<Block> blacklist = Arrays.asList(Blocks.air, Blocks.water, Blocks.torch,
            Blocks.redstone_torch, Blocks.ladder, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
            Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
            Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.web, Blocks.redstone_torch, Blocks.brewing_stand,
            Blocks.waterlily, Blocks.farmland, Blocks.sand, Blocks.beacon, Blocks.double_plant, Blocks.noteblock,
            Blocks.hopper, Blocks.dispenser, Blocks.dropper, Blocks.crafting_table, Blocks.command_block);

    public Scaffold() {
        super("Scaffold", ModuleType.World);
        this.addValues(this.swing, this.tower, this.towerMove, this.silent, this.down, this.mark, this.mode);
        new ReVerify();
    }

    @Override
    public void onEnable() {
        this.y = mc.thePlayer.posY;
        this.lastYaw = mc.thePlayer.rotationYaw;
        this.lastPitch = mc.thePlayer.rotationPitch;
        this.blockYaw = this.getRotation()[0];
        this.blockPitch = 80.0f;
        mc.timer.timerSpeed = 1.0F;
        this.slot = mc.thePlayer.inventory.currentItem;
        this.b = false;
        if (this.slot != this.getBlockSlot()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.getBlockSlot()));
        }
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer.isSwingInProgress) {
            mc.thePlayer.swingProgress = 0.0f;
            mc.thePlayer.swingProgressInt = 0;
            mc.thePlayer.isSwingInProgress = false;
        }
        if (mc.thePlayer.inventory.currentItem != this.slot) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            this.slot = mc.thePlayer.inventory.currentItem;
        }
        this.lastYaw = mc.thePlayer.rotationYaw;
        this.lastPitch = mc.thePlayer.rotationPitch;
        this.blockYaw = mc.thePlayer.rotationYaw;
        this.blockPitch = mc.thePlayer.rotationPitch;
        mc.timer.timerSpeed = 1.0F;
    }

    @EventHandler
    private void onRender(EventRender2D e) {
        int width = new ScaledResolution(mc).getScaledWidth() / 2;
        int height = new ScaledResolution(mc).getScaledHeight() / 2;
        int color = new Color(255, 0, 0).getRGB();
        if (this.getBlockCount() > 10 && 512 >= this.getBlockCount()) {
            color = new Color(255, 255, 0).getRGB();
        } else if (this.getBlockCount() > 512) {
            color = new Color(0, 255, 0).getRGB();
        }
        Client.fontLoaders.msFont18.drawOutlinedString("Block: " + this.getBlockCount(),
                width - Client.fontLoaders.msFont18.getStringWidth("Block: " + this.getBlockCount()) / 2, height - 8,
                color);
    }

    @EventHandler
    public void onPre(EventPreUpdate event) {
        this.setSuffix(mode.getValue());
        if (mode.getValue().toString().equals("AAC")) {
            mc.thePlayer.setSprinting(false);
        }

        this.moveBlock();
        this.shouldDown = mc.gameSettings.keyBindAttack.isKeyDown() && PlayerUtils.isMoving() && this.down.getValue();
        double x = mc.thePlayer.posX;
        double z = mc.thePlayer.posZ;
        if (this.mode.getValue().toString().equals("Hypixel")) {
            if (this.shouldDown) {
                mc.thePlayer.setSprinting(false);
            }
            if (!ModuleManager.getModuleByClass(Speed.class).isEnabled() || mc.gameSettings.keyBindJump.isKeyDown()
                    || mc.thePlayer.onGround) {
                this.y = mc.thePlayer.posY;
                this.shouldSafeWalk = !this.shouldDown;
            } else {
                this.shouldSafeWalk = false;
            }
        }
        if (mode.getValue().toString().equals("Hypixel")) {
            // mc.thePlayer.setSprinting(false);
        }
        if (this.isAirBlock(
                mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ))
                        .getBlock())) {
            x = mc.thePlayer.posX;
            z = mc.thePlayer.posZ;
        }
        BlockPos blockPos = new BlockPos(x, this.y - 1.0 - (this.shouldDown ? 0.01 : 0.0), z);
        this.block = this.getBlockByPos(blockPos);
        this.blockData = this.getBlockData(blockPos);
        if (this.getBlockCount() <= 0) {
            return;
        }
        this.doMotionUp();
        if (!mode.getValue().toString().equals("Normal")) {
            this.doRotate(event);
        }
        if (this.blockData != null) {
            if (this.isAirBlock(this.block)) {
//				if (!mode.getValue().toString().equals("Hypixel")) {
//					this.blockYaw = getRotations(blockData.position, blockData.face)[0];
//					this.blockPitch = getRotations(blockData.position, blockData.face)[1];
//				} else {
                // float yaw = RotationUtil.getRotations(mc.thePlayer.posX, mc.thePlayer.posY,
                // mc.thePlayer.posZ, mc.thePlayer.getEyeHeight(), blockData.position,
                // blockData.face)[0];
                // if ((this.blockYaw - yaw > 30.0f && yaw - this.blockYaw < 0) || (yaw -
                // this.blockYaw > 30.0f && this.blockYaw - yaw < 0)) {
                // this.blockYaw = yaw;
                // }
                this.blockYaw = getRotations(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
                        mc.thePlayer.getEyeHeight(), blockData.position, blockData.face)[0];
                this.blockPitch = getRotations(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
                        mc.thePlayer.getEyeHeight(), blockData.position, blockData.face)[1];

//				if (mode.getValue().toString().equals("Normal") || (!PlayerUtils.isMoving() && mc.thePlayer.motionY < 0)) {
//					this.doRotate(event, blockData);
//				}
            }
        }
    }

    public static float[] getRotations(double posX, double posY, double posZ, double eyeHeight, final BlockPos blockPos,
                                       final EnumFacing enumFacing) {
        double n = blockPos.getX() + 0.5 - posX + enumFacing.getFrontOffsetX() / 2.0;
        double n2 = blockPos.getZ() + 0.5 - posZ + enumFacing.getFrontOffsetZ() / 2.0;
        double n3 = posY + eyeHeight - (blockPos.getY() + 0.5);
        double n4 = MathHelper.sqrt_double(n * n + n2 * n2);
        float n5 = (float) (Math.atan2(n2, n) * 180.0 / 3.141592653589793) - 90.0f;
        float n6 = (float) (Math.atan2(n3, n4) * 180.0 / 3.141592653589793);
        if (n5 < 0.0f) {
            n5 += 360.0f;
        }
        return new float[]{n5, n6};
    }

    private void doRotate(EventPreUpdate e) {
//        float[] rotation = getRotations(blockData.position);
//		if (mode.getValue().toString().equals("Hypixel")) {
//			this.blockYaw = rotation[0];
//			this.blockPitch = rotation[1];
//		}
//		e.setYaw(rotation[0]);
//		e.setPitch(rotation[1]);
        if (mode.getValue().toString().equals("AAC")) {
            this.lastYaw = this.getRotation()[0];
            this.lastPitch = this.getRotation()[1];
            e.setYaw(this.getRotation()[0]);
            e.setPitch(this.getRotation()[1]);
        } else {
            this.lastYaw = this.blockYaw;
            this.lastPitch = this.blockPitch;
            float current = this.blockYaw;
            ArrayList<java.lang.Float> distances = new ArrayList<>();
            for (float rotation : new float[]{mc.thePlayer.rotationYaw - 180.0f, mc.thePlayer.rotationYaw - 225.0f,
                    mc.thePlayer.rotationYaw - 135.0f, mc.thePlayer.rotationYaw, mc.thePlayer.rotationYaw - 315.0f,
                    mc.thePlayer.rotationYaw - 45.0f, mc.thePlayer.rotationYaw - 270.0f,
                    mc.thePlayer.rotationYaw - 90.0f}) {
                distances.add(Math.abs(rotation - current));
            }
            distances.sort(Comparator.comparing(f1 -> f1));
            e.setYaw(Math.abs(distances.get(0) - current) >= 150.0f ? current : distances.get(0));
            e.setPitch(this.blockPitch);
        }
    }

//	private void doRotate(EventPreUpdate e, BlockData blockData) {
//		float[] rotation = getRotations(blockData.position, blockData.face);
//		if (mode.getValue().toString().equals("Hypixel")) {
//			this.blockYaw = rotation[0];
//			this.blockPitch = rotation[1];
//		}
//		e.setYaw(rotation[0]);
//		e.setPitch(rotation[1]);
//		if (Float.getModuleManager().getModuleByClass(Rotate.class).isEnabled() && Rotate.scaffold.getValue()) {
//			new RenderRotate(rotation[0], rotation[1]);
//		}
//	}

    private float getPitch() {
        return this.blockPitch;
    }

    /*
     * private float[] getRotation() { if
     * (mc.gameSettings.keyBindForward.isKeyDown()) { if
     * (mc.thePlayer.movementInput.moveStrafe == 0.0) { return
     * RotationUtil.getRotateForScaffold(mc.thePlayer.rotationYaw - 180.0f,
     * this.getPitch(), this.lastYaw, this.lastPitch); } else if
     * (mc.gameSettings.keyBindLeft.isKeyDown()) { return
     * RotationUtil.getRotateForScaffold(mc.thePlayer.rotationYaw - 225.0f,
     * this.getPitch(), this.lastYaw, this.lastPitch); } else if
     * (mc.gameSettings.keyBindRight.isKeyDown()){ return
     * RotationUtil.getRotateForScaffold(mc.thePlayer.rotationYaw - 135.0f,
     * this.getPitch(), this.lastYaw, this.lastPitch); } } else if
     * (mc.gameSettings.keyBindBack.isKeyDown()) { if
     * (mc.thePlayer.movementInput.moveStrafe == 0.0) { return
     * RotationUtil.getRotateForScaffold(mc.thePlayer.rotationYaw, this.getPitch(),
     * this.lastYaw, this.lastPitch); } else if
     * (mc.gameSettings.keyBindLeft.isKeyDown()) { return
     * RotationUtil.getRotateForScaffold(mc.thePlayer.rotationYaw - 315.0f,
     * this.getPitch(), this.lastYaw, this.lastPitch); } else if
     * (mc.gameSettings.keyBindRight.isKeyDown()) { return
     * RotationUtil.getRotateForScaffold(mc.thePlayer.rotationYaw - 45.0f,
     * this.getPitch(), this.lastYaw, this.lastPitch); } } else if
     * (mc.gameSettings.keyBindLeft.isKeyDown()) { return
     * RotationUtil.getRotateForScaffold(mc.thePlayer.rotationYaw - 270.0f,
     * this.getPitch(), this.lastYaw, this.lastPitch); } else if
     * (mc.gameSettings.keyBindRight.isKeyDown()) { return
     * RotationUtil.getRotateForScaffold(mc.thePlayer.rotationYaw - 90.0f,
     * this.getPitch(), this.lastYaw, this.lastPitch); } return
     * RotationUtil.getRotateForScaffold(mc.thePlayer.rotationYaw, this.getPitch(),
     * this.lastYaw, this.lastPitch); }
     */

    private float[] getRotation() {
        if (mc.gameSettings.keyBindForward.isKeyDown()) {
            if (mc.thePlayer.movementInput.moveStrafe == 0.0) {
                return new float[]{mc.thePlayer.rotationYaw - 180.0f, this.getPitch()};
            } else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                return new float[]{mc.thePlayer.rotationYaw - 225.0f, this.getPitch()};
            } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
                return new float[]{mc.thePlayer.rotationYaw - 135.0f, this.getPitch()};
            }
        } else if (mc.gameSettings.keyBindBack.isKeyDown()) {
            if (mc.thePlayer.movementInput.moveStrafe == 0.0) {
                return new float[]{mc.thePlayer.rotationYaw, this.getPitch()};
            } else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                return new float[]{mc.thePlayer.rotationYaw - 315.0f, this.getPitch()};
            } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
                return new float[]{mc.thePlayer.rotationYaw - 45.0f, this.getPitch()};
            }
        } else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
            return new float[]{mc.thePlayer.rotationYaw - 270.0f, this.getPitch()};
        } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
            return new float[]{mc.thePlayer.rotationYaw - 90.0f, this.getPitch()};
        }
        return new float[]{this.blockYaw, this.blockPitch};
    }

    private void doMotionUp() {
        if (this.getBlockCount() > 0 && this.tower.getValue() && (!PlayerUtils.isMoving() || towerMove.getValue())) {
            if (!mc.gameSettings.keyBindJump.isKeyDown()) {
                if (PlayerUtils.isMoving() && !ModuleManager.getModuleByClass(Speed.class).isEnabled()) {
                    if (PlayerUtils.isOnGround(0.76) && !PlayerUtils.isOnGround(0.75) && mc.thePlayer.motionY > 0.23
                            && mc.thePlayer.motionY < 0.25) {
                        mc.thePlayer.motionY = Math.round(mc.thePlayer.posY) - mc.thePlayer.posY;
                    }
                    if (!PlayerUtils.isOnGround(1.0E-4)) {
                        if (mc.thePlayer.motionY > 0.1 && mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 1.0E-4
                                && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 1.0E-4) {
                            mc.thePlayer.motionY = 0.0;
                        }
                    }
                }
                // mc.timer.timerSpeed = 1.0F;
                return;
            }
            ++this.towerTick;
            if (PlayerUtils.isMoving()) {
                if (PlayerUtils.isOnGround(0.76) && !PlayerUtils.isOnGround(0.75) && mc.thePlayer.motionY > 0.23
                        && mc.thePlayer.motionY < 0.25) {
                    mc.thePlayer.motionY = Math.round(mc.thePlayer.posY) - mc.thePlayer.posY;
                }
                if (PlayerUtils.isOnGround(1.0E-4)) {
                    mc.thePlayer.motionY = mc.gameSettings.keyBindForward.isKeyDown() ? 0.4189999999999 : 0.38F;
                    mc.thePlayer.motionX *= 0.95D;
                    mc.thePlayer.motionZ *= 0.95D;
                } else if (!PlayerUtils.isOnGround(1.0E-4)
                        && mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 1.0E-4
                        && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 1.0E-4) {
                    mc.thePlayer.motionY = 0.0;
                }
            }
        }
    }

    private int moveHot() {
        int slot = -1;
        for (int i = 36; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item instanceof ItemBlock && this.isValid(item) && is.stackSize > slot) {
                    slot = i;
                    return slot;
                }
            }
        }
        return slot;
    }

    private boolean hotBlockError() {
        int i = 36;
        while (i < 45) {
            try {
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemBlock)
                        || !isValid(stack.getItem())) {
                    i++;
                    continue;
                }
                return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private void moveBlock() {
        if (this.getBlockCount() == 0)
            return;
        int slot = -1;
        int size = 0;
        getBlockCount();
        for (int i = 9; i < 36; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item instanceof ItemBlock && this.isValid(item) && is.stackSize > size) {
                    size = is.stackSize;
                    slot = i;
                }
            }
        }
        for (int i = 36; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                if (item instanceof ItemBlock) {
                    this.isValid(item);
                }
            }
        }
        ItemStack is = new ItemStack(Item.getItemById(261));
        int bestInvSlot = slot;
        int bestHotbarSlot = moveHot();
        if (bestHotbarSlot > 0 && bestInvSlot > 0 && mc.thePlayer.inventoryContainer.getSlot(bestInvSlot).getHasStack()
                && mc.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getHasStack()) {
            mc.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getStack();
            mc.thePlayer.inventoryContainer.getSlot(bestInvSlot).getStack();
        }
        if (hotBlockError()) {
            for (int a = 36; a < 45; a++) {
                if (!mc.thePlayer.inventoryContainer.getSlot(a).getHasStack()) {
                    break;
                }
            }
        } else {
            for (int i = 9; i < 36; i++) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                    int count = 0;
                    if (item instanceof ItemBlock && isValid(item)) {
                        for (int a = 36; a < 45; a++) {
                            if (Container.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(a), is, true)) {
                                swap(i, a - 36);
                                count++;
                                break;
                            }
                        }
                        if (count == 0) {
                            swap(i, 7);
                        }
                        break;
                    }
                }
            }
        }
    }

    private void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    @EventHandler
    public void onMove(EventMove e) {
        if (this.mode.getValue().toString().equals("Hypixel")) {
            if (this.shouldDown && !mc.thePlayer.onGround) {
                e.setMoveSpeed(0.039838);
            } else {
                if (PlayerUtils.isMoving()) {
                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        e.setX(e.getX() * (1.0
                                - 0.08 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1)));
                        e.setZ(e.getZ() * (1.0
                                - 0.08 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1)));
                    }
                    if (mc.thePlayer.ticksExisted % 3 == 0) {

                        // mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer,
                        // C0BPacketEntityAction.Action.STOP_SNEAKING));
                        e.setX(e.getX() * 0.72f);
                        e.setZ(e.getZ() * 0.72f);
                        mc.timer.timerSpeed = 1.5f;
                        // mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer,
                        // C0BPacketEntityAction.Action.STOP_SNEAKING));
                    } else {
                        e.setX(e.getX() * 0.62f);
                        e.setZ(e.getZ() * 0.62f);
                        mc.timer.timerSpeed = 1.45f;
                    }
                } else {
                    e.setX(0.0f);
                    e.setZ(0.0f);
                    mc.timer.timerSpeed = 1f;
                }
            }
        }
    }

    @EventHandler
    public void onPostUpdate(EventPostUpdate eventPostUpdate) {
        if ((!this.timer.delay(80L) && PlayerUtils.isOnGround(0.01) && !mc.gameSettings.keyBindJump.isKeyDown())
                || this.getBlockCount() <= 0) {
            return;
        }
        this.timer.reset();
        this.b = this.slot != this.getBlockSlot() && silent.getValue();
        boolean b2 = false;
        if (blockData != null) {
            if (this.isAirBlock(this.block)) {
                if (this.autoBlockCoolDown.delay(500L)) {
                    if (this.b) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.getBlockSlot()));
                        b2 = true;
                    }
                }
                if (b2 || this.slot == this.getBlockSlot()) {
                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
                            mc.thePlayer.inventory.getStackInSlot(this.slot), blockData.position, blockData.face,
                            getVec3(blockData.position, blockData.face))) {
                        if (swing.getValue()) {
                            mc.thePlayer.swingItem();
                        } else if (!mode.getValue().toString().equals("Normal")) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                        }
                    }
                }
                if (this.timer.delay(250L)) {
                    this.timer.reset();
                }
            }
        }
    }

    private int getBlockSlot() {
        int slot = -1;
        if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
            for (int i = 36; i < 45; ++i) {
                slot = i - 36;
                if (!Container.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(i),
                        new ItemStack(Item.getItemById(261)), true)
                        && mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock
                        && mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null
                        && this.isValid(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())
                        && mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize != 0) {
                    break;
                }
            }
        } else {
            slot = mc.thePlayer.inventory.currentItem;
        }
        return slot;
    }

    @EventHandler
    public void onPacketSend(EventPacketSend e) {
        if (e.getPacket() instanceof C09PacketHeldItemChange) {
            this.slot = ((C09PacketHeldItemChange) e.getPacket()).getSlotId();
        } else if (e.getPacket() instanceof C07PacketPlayerDigging) {
            if (this.slot != mc.thePlayer.inventory.currentItem) {
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                this.slot = mc.thePlayer.inventory.currentItem;
            }
        }
    }

    private Vec3 getVec3(BlockPos blockPos, EnumFacing enumFacing) {
        double n = blockPos.getX() + 0.5;
        double n2 = blockPos.getY() + 0.5;
        double n3 = blockPos.getZ() + 0.5;
        double n4 = n + enumFacing.getFrontOffsetX() / 2.0;
        double n5 = n3 + enumFacing.getFrontOffsetZ() / 2.0;
        double n6 = n2 + enumFacing.getFrontOffsetY() / 2.0;
        if (enumFacing == EnumFacing.UP || enumFacing == EnumFacing.DOWN) {
            n4 += Math.random() * 0.6 - 0.3;
            n5 += Math.random() * 0.6 - 0.3;
        } else {
            n6 += Math.random() * 0.6 - 0.3;
        }
        if (enumFacing == EnumFacing.WEST || enumFacing == EnumFacing.EAST) {
            n5 += Math.random() * 0.6 - 0.3;
        }
        if (enumFacing == EnumFacing.SOUTH || enumFacing == EnumFacing.NORTH) {
            n4 += Math.random() * 0.6 - 0.3;
        }
        return new Vec3(n4, n6, n5);
    }

    private double[] getExpandCords(double xIn, double zIn, double forward, double strafe, float yaw) {
        Block block = this.getBlockByPos(new BlockPos(xIn, mc.thePlayer.posY - 1.0, zIn));
        double x = mc.thePlayer.posX;
        double z = mc.thePlayer.posZ;
        double value = 0.0;
        double max = 1.2;
        while (!this.isAirBlock(block)) {
            ++value;
            if (value > max) {
                value = max;
            }
            double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            x = xIn + (forward * 0.45 * cos + strafe * 0.45 * sin) * value;
            z = zIn + (forward * 0.45 * sin - strafe * 0.45 * cos) * value;
            if (value == max) {
                break;
            }
            block = this.getBlockByPos(new BlockPos(x, mc.thePlayer.posY - 1.0, z));
        }
        return new double[]{x, z};
    }

    private boolean isValid(Item item) {
        return item instanceof ItemBlock && !blacklist.contains(((ItemBlock) item).getBlock());
    }

    private Block getBlockByPos(BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos).getBlock();
    }

    private boolean isNotAir(BlockPos blockPos) {
        return this.getBlockByPos(blockPos) != Blocks.air && !(this.getBlockByPos(blockPos) instanceof BlockLiquid);
    }

    private BlockData getBlockData(BlockPos down) {
        BlockData blockData = null;
        int n = 0;
        while (true) {
            if (n >= 2) {
                break;
            }
            if (this.isNotAir(down.add(0, 0, 1))) {
                blockData = new BlockData(down.add(0, 0, 1), EnumFacing.NORTH);
                break;
            }
            if (this.isNotAir(down.add(0, 0, -1))) {
                blockData = new BlockData(down.add(0, 0, -1), EnumFacing.SOUTH);
                break;
            }
            if (this.isNotAir(down.add(1, 0, 0))) {
                blockData = new BlockData(down.add(1, 0, 0), EnumFacing.WEST);
                break;
            }
            if (this.isNotAir(down.add(-1, 0, 0))) {
                blockData = new BlockData(down.add(-1, 0, 0), EnumFacing.EAST);
                break;
            }
            if (this.isNotAir(down.add(0, -1, 0))) {
                blockData = new BlockData(down.add(0, -1, 0), EnumFacing.UP);
                break;
            }
            if (this.isNotAir(down.add(0, 1, 0)) && this.shouldDown) {
                blockData = new Scaffold.BlockData(down.add(0, 1, 0), EnumFacing.DOWN);
                break;
            }
            if (this.isNotAir(down.add(0, 1, 1)) && this.shouldDown) {
                blockData = new Scaffold.BlockData(down.add(0, 1, 1), EnumFacing.DOWN);
                break;
            }
            if (this.isNotAir(down.add(0, 1, -1)) && this.shouldDown) {
                blockData = new Scaffold.BlockData(down.add(0, 1, -1), EnumFacing.DOWN);
                break;
            }
            if (this.isNotAir(down.add(1, 1, 0)) && this.shouldDown) {
                blockData = new Scaffold.BlockData(down.add(1, 1, 0), EnumFacing.DOWN);
                break;
            }
            if (this.isNotAir(down.add(-1, 1, 0)) && this.shouldDown) {
                blockData = new Scaffold.BlockData(down.add(-1, 1, 0), EnumFacing.DOWN);
                break;
            }
            if (this.isNotAir(down.add(1, 0, 1))) {
                blockData = new BlockData(down.add(1, 0, 1), EnumFacing.NORTH);
                break;
            }
            if (this.isNotAir(down.add(-1, 0, -1))) {
                blockData = new BlockData(down.add(-1, 0, -1), EnumFacing.SOUTH);
                break;
            }
            if (this.isNotAir(down.add(1, 0, 1))) {
                blockData = new BlockData(down.add(1, 0, 1), EnumFacing.WEST);
                break;
            }
            if (this.isNotAir(down.add(-1, 0, -1))) {
                blockData = new BlockData(down.add(-1, 0, -1), EnumFacing.EAST);
                break;
            }
            if (this.isNotAir(down.add(-1, 0, 1))) {
                blockData = new BlockData(down.add(-1, 0, 1), EnumFacing.NORTH);
                break;
            }
            if (this.isNotAir(down.add(1, 0, -1))) {
                blockData = new BlockData(down.add(1, 0, -1), EnumFacing.SOUTH);
                break;
            }
            if (this.isNotAir(down.add(1, 0, -1))) {
                blockData = new BlockData(down.add(1, 0, -1), EnumFacing.WEST);
                break;
            }
            if (this.isNotAir(down.add(-1, 0, 1))) {
                blockData = new BlockData(down.add(-1, 0, 1), EnumFacing.EAST);
                break;
            }
            down = down.down();
            ++n;
        }
        return blockData;
    }

    public boolean isAirBlock(Block block) {
        return block.getMaterial().isReplaceable()
                && (!(block instanceof BlockSnow) || block.getBlockBoundsMaxY() <= 0.125);
    }

    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && !blacklist.contains(((ItemBlock) item).getBlock())) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    static class BlockData {
        public BlockPos position;
        public EnumFacing face;

        private BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}
