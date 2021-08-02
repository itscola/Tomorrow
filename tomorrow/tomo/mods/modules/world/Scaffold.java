package tomorrow.tomo.mods.modules.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.rendering.EventRender3D;
import tomorrow.tomo.event.events.world.EventPostUpdate;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.event.value.Value;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.math.MathUtil;
import tomorrow.tomo.utils.math.RotationUtil;
import tomorrow.tomo.utils.render.RenderUtil;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Scaffold extends Module {
	private Option tower = new Option("Tower", "tower", Boolean.valueOf(true));
	private Option silent = new Option("Silent", "Silent", Boolean.valueOf(true));
	private Option aac = new Option("AAC", "AAC", Boolean.valueOf(false));
	private Mode mod = new Mode("Mode", "Mode", new String[] { "Hypixel", "NCP", "AAC4" }, "Hypixel");
	private List invalid;
	private BlockCache blockCache;
	private int currentItem;

	public Scaffold() {
		super("Scaffold", ModuleType.World);
		this.invalid = Arrays.asList(new Block[] { Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water,
				Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.enchanting_table, Blocks.tnt });
		this.addValues(new Value[] { this.tower, this.silent, this.aac, this.mod });
		this.currentItem = 0;
	}

	public void onEnable() {
		if (mc.theWorld != null) {
			this.currentItem = this.mc.thePlayer.inventory.currentItem;
		}
	}

	public void onDisable() {
		this.mc.thePlayer.inventory.currentItem = this.currentItem;
	}

	// 在这转头
	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		if (((Boolean) this.aac.getValue()).booleanValue()) {
			this.mc.thePlayer.setSprinting(false);
		}

		if (mod.getValue().equals("AAC4")) {
			if (this.grabBlockSlot() != -1) {
				this.blockCache = this.grab();
			}
			if (this.blockCache != null || ((boolean) silent.getValue())) {
				event.setYaw(mc.thePlayer.rotationYaw + 180);
				event.setPitch(80);
			}
		}
		if (mod.getValue().equals("NCP")) {
			if (this.grabBlockSlot() != -1) {
				this.blockCache = this.grab();
				float[] rotations = new float[0];
				if (this.blockCache != null) {
					rotations = RotationUtil.grabBlockRotations(blockCache.getPosition());
				}
				if (this.blockCache != null || ((boolean) silent.getValue()) && rotations.length != 0) {
					event.setYaw(rotations[0]);
					event.setPitch(RotationUtil
							.getVecRotation(this.grabPosition(blockCache.getPosition(), blockCache.getFacing()))[1]
							- 3.0F);
				}
			}
		}
//
		if (mod.getValue().equals("Hypixel")) {
			if (this.grabBlockSlot() != -1) {
				this.blockCache = this.grab();
				float[] rotations = new float[0];
				if (this.blockCache != null) {
					rotations = RotationUtil.grabBlockRotations(blockCache.getPosition());
				}
				if (this.blockCache != null || ((boolean) silent.getValue()) && rotations.length != 0) {
					event.setYaw(rotations[0]);
					event.setPitch(RotationUtil
							.getVecRotation(this.grabPosition(blockCache.getPosition(), blockCache.getFacing()))[1]
							);
				}
			}
		}
	}

	// Tower以及inventory
	@EventHandler
	private void onPostUpdate(EventPostUpdate event) {
		
		if (this.blockCache != null) {
			if (this.mc.gameSettings.keyBindJump.pressed && ((Boolean) this.tower.getValue()).booleanValue()){
			mc.timer.timerSpeed = 1.2f;
			}else {
				mc.timer.timerSpeed = 1f;
			}
			if (this.mc.gameSettings.keyBindJump.pressed && ((Boolean) this.tower.getValue()).booleanValue() && mc.thePlayer.onGround) {
				this.mc.thePlayer.setSprinting(false);
				this.mc.thePlayer.motionY = 0;
				this.mc.thePlayer.motionX = 0.0D;
				this.mc.thePlayer.motionZ = 0.0D;
				mc.thePlayer.jump();
			}

			int currentSlot = this.mc.thePlayer.inventory.currentItem;
			int slot = this.grabBlockSlot();
			this.mc.thePlayer.inventory.currentItem = slot;
			if (this.placeBlock(blockCache.getPosition(), blockCache.getFacing())) {
				if (((Boolean) this.silent.getValue()).booleanValue()) {
					this.mc.thePlayer.inventory.currentItem = currentSlot;
					this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentSlot));
				}

//                this.blockCache = null;
			}

		}
	}

	Vec3 v = null;

	@EventHandler
	public void onRender3D(EventRender3D e) {
		if (blockCache != null) {
			v = ((new Vec3(blockCache.getPosition())).addVector(0.5D, 0.5D, 0.5D)
					.add((new Vec3(blockCache.getFacing().getDirectionVec()).scale(1.5f))));
			if (blockCache.getFacing().getDirectionVec().getX() != 0) {
				v = v.addVector(0, 0, mc.thePlayer.getPos().getZ() - v.zCoord);
			} else if (blockCache.getFacing().getDirectionVec().getZ() != 0) {
				v = v.addVector(mc.thePlayer.getPos().getX() - v.xCoord, 0, 0);
			}
			v = v.addVector(mc.thePlayer.getPos().getX() - v.xCoord, MathUtil.randomDouble(0D, 0.1D), 0);

			double x = v.getX() - RenderManager.renderPosX - 0.1f;
			double y = v.getY() - RenderManager.renderPosY - 0.1f;
			double z = v.getZ() - RenderManager.renderPosZ - 0.1f;
//            System.out.println(x+ " " + y + " " +z);

		}
		if (v != null) {
			double x = v.getX() - RenderManager.renderPosX;
			double y = v.getY() - RenderManager.renderPosY;
			double z = v.getZ() - RenderManager.renderPosZ;

			double x2 = v.getX() - RenderManager.renderPosX + 0.1f;
			double y2 = v.getY() - RenderManager.renderPosY + 0.1f;
			double z2 = v.getZ() - RenderManager.renderPosZ + 0.1f;
			double width = 0.3;
			double height = mc.thePlayer.getEyeHeight();
			RenderUtil.pre3D();
			GL11.glLoadIdentity();
			mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
			RenderUtil.glColor(new Color(255, 0, 0).getRGB());

			GL11.glLineWidth(4);

			GL11.glBegin(GL11.GL_LINE_STRIP);
			GL11.glVertex3d(x, y + 0.2, z);
			GL11.glVertex3d(x, y - 0.2, z);

			GL11.glEnd();

			GL11.glBegin(GL11.GL_LINE_STRIP);
			GL11.glVertex3d(x + 0.2, y, z);
			GL11.glVertex3d(x - 0.2, y, z);
			GL11.glEnd();

			GL11.glBegin(GL11.GL_LINE_STRIP);
			GL11.glVertex3d(x, y, z - 0.2);
			GL11.glVertex3d(x, y, z + 0.2);
			GL11.glEnd();

			RenderUtil.post3D();
		}
	}

	private boolean placeBlock(BlockPos pos, EnumFacing facing) {
//        new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double) this.mc.thePlayer.getEyeHeight(), this.mc.thePlayer.posZ);

		if (this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld,
				this.mc.thePlayer.getHeldItem(), pos, facing, v)) {
			this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
			return true;
		} else {
			return false;
		}
	}

	private Vec3 grabPosition(BlockPos position, EnumFacing facing) {
		Vec3 offset = new Vec3((double) facing.getDirectionVec().getX() / 2.0D,
				(double) facing.getDirectionVec().getY() / 2.0D, (double) facing.getDirectionVec().getZ() / 2.0D);
		Vec3 point = new Vec3((double) position.getX() + 0.5D, (double) position.getY() + 0.5D,
				(double) position.getZ() + 0.5D);
		return point.add(offset);
	}

	private BlockCache grab() {
		EnumFacing[] invert = new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH,
				EnumFacing.EAST, EnumFacing.WEST };
		BlockPos position = (new BlockPos(this.mc.thePlayer.getPositionVector())).offset(EnumFacing.DOWN);

		EnumFacing[] var6;
		int var5 = (var6 = EnumFacing.values()).length;

		for (int offset = 0; offset < var5; ++offset) {
			EnumFacing offsets = var6[offset];
			BlockPos offset1 = position.offset(offsets);
			this.mc.theWorld.getBlockState(offset1);
			if (!(this.mc.theWorld.getBlockState(offset1).getBlock() instanceof BlockAir)) {
				return new BlockCache(offset1, invert[offsets.ordinal()]);
			}
		}

		BlockPos[] var16 = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1),
				new BlockPos(0, 0, 1) };
		if (this.mc.thePlayer.onGround) {
			BlockPos[] var19 = var16;
			int var18 = var16.length;

			for (var5 = 0; var5 < var18; ++var5) {
				BlockPos var17 = var19[var5];
				BlockPos offsetPos = position.add(var17.getX(), 0, var17.getZ());
				this.mc.theWorld.getBlockState(offsetPos);
				if (this.mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
					EnumFacing[] var13;
					int var12 = (var13 = EnumFacing.values()).length;

					for (int var11 = 0; var11 < var12; ++var11) {
						EnumFacing facing2 = var13[var11];
						BlockPos offset2 = offsetPos.offset(facing2);
						this.mc.theWorld.getBlockState(offset2);
						if (!(this.mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir)) {
							return new BlockCache(offset2, invert[facing2.ordinal()]);
						}
					}
				}
			}
		}

		return null;

	}

	private int grabBlockSlot() {
		for (int i = 0; i < 9; ++i) {
			ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
			if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
				return i;
			}
		}

		return -1;
	}
}

class BlockCache {
	private BlockPos position;
	private EnumFacing facing;

	public BlockCache(BlockPos position, EnumFacing facing) {
		this.position = position;
		this.facing = facing;
	}

	public BlockPos getPosition() {
		return this.position;
	}

	public EnumFacing getFacing() {
		return this.facing;
	}

}
