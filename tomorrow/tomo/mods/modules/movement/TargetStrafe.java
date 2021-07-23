/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package tomorrow.tomo.mods.modules.movement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.rendering.EventRender3D;
import tomorrow.tomo.event.events.world.EventMove;
import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.mods.modules.combat.Killaura;
import tomorrow.tomo.utils.cheats.player.PlayerUtils;
import tomorrow.tomo.utils.entity.EntityValidator;
import tomorrow.tomo.utils.entity.impl.VoidCheck;
import tomorrow.tomo.utils.entity.impl.WallCheck;
import tomorrow.tomo.utils.render.RenderUtil;

import java.awt.*;

public final class TargetStrafe
        extends Module {
    private final Numbers<Number> radius = new Numbers<Number>("Radius", 2.0, 0.1, 4.0, 0.1);
    private final Option render = new Option("Render", true);
    private final Option directionKeys = new Option("Direction Keys", true);
    private final Option space = new Option("Hold Space", false);
    private final EntityValidator targetValidator;
    private Killaura aura;
    private int direction = -1;

    public TargetStrafe() {
        super("TargetStrafe", ModuleType.Movement);
        this.addValues(this.radius, this.render, this.directionKeys, this.space);
        this.targetValidator = new EntityValidator();
        this.targetValidator.add(new VoidCheck());
        this.targetValidator.add(new WallCheck());
    }

    @Override
    public void onEnable() {
        if (this.aura == null) {
            this.aura = ((Killaura) ModuleManager.getModuleByClass(Killaura.class));
        }
    }

    @EventHandler
    public final void onUpdate(EventMove event) {
        if (mc.thePlayer.isCollidedHorizontally || (!new VoidCheck().validate(mc.thePlayer) && ((boolean) this.space.getValue()))) {
            this.switchDirection();
        }
        if (mc.gameSettings.keyBindLeft.isPressed()) {
            this.direction = 1;
        }
        if (mc.gameSettings.keyBindRight.isPressed()) {
            this.direction = -1;
        }
    }

    private void switchDirection() {
        this.direction = this.direction == 1 ? -1 : 1;
    }

    public void strafe(EventMove event, double moveSpeed) {
        if (canStrafe()) {
        EntityLivingBase target = this.aura.target;
        float[] rotations = PlayerUtils.getRotations(target);
            if ((double) mc.thePlayer.getDistanceToEntity(target) <= this.radius.getValue().floatValue()) {
                PlayerUtils.setSpeed(event, moveSpeed, rotations[0], this.direction, 0.0);
            } else {
                PlayerUtils.setSpeed(event, moveSpeed, rotations[0], this.direction, 1.0);
            }
        }
    }

    @EventHandler
    public void onRender3D(EventRender3D event) {
        if (this.canStrafe() && ((boolean) this.render.getValue())) {
            this.drawCircle(Killaura.target, event.getPartialTicks(), this.radius.getValue().floatValue());
        }
    }

    private void drawCircle(Entity entity, float partialTicks, double rad) {
        GL11.glPushMatrix();
        GL11.glDisable((int) 3553);
        RenderUtil.startSmooth();
        GL11.glDisable((int) 2929);
        GL11.glDepthMask((boolean) false);
        GL11.glLineWidth((float) 1.5f);
        GL11.glBegin((int) 3);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks - mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks - mc.getRenderManager().viewerPosZ;

        double pix2 = Math.PI * 2;
        int tick = 0;
        for (int i = 0; i <= 90; ++i) {
            Color c = new Color(Color.HSBtoRGB(tick / 45f + mc.thePlayer.ticksExisted / 50f, 0.7f, 1));
            GL11.glColor3f(c.getRed() / 255f, (float) c.getGreen() / 255f, (float) c.getBlue() / 255f);
            GL11.glVertex3d((double) (x + rad * Math.cos((double) i * (Math.PI * 2) / 45.0)), (double) y, (double) (z + rad * Math.sin((double) i * (Math.PI * 2) / 45.0)));
            tick++;
        }
        GL11.glEnd();
        GL11.glDepthMask((boolean) true);
        GL11.glEnable((int) 2929);
        RenderUtil.endSmooth();
        GL11.glEnable((int) 3553);
        GL11.glPopMatrix();
    }

    public boolean canStrafe() {
        return this.aura.isEnabled() && Killaura.target != null && this.isEnabled();
    }
}

