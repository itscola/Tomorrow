/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package tomorrow.tomo.mods.modules.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import tomorrow.tomo.managers.FriendManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.math.Vec3f;
import tomorrow.tomo.utils.render.RenderUtil;
import tomorrow.tomo.utils.render.gl.GLUtils;

import java.awt.*;
import java.util.ArrayList;

public class ESP
extends Module {
    private ArrayList<Vec3f> points = new ArrayList();
    public ESP() {
        super("ESP", ModuleType.Render);
        int i = 0;
        while (i < 8) {
            this.points.add(new Vec3f());
            ++i;
        }
    }

    private void render(Entity entity) {
        Entity extended = entity;
        RenderManager renderManager = this.mc.getRenderManager();
        Vec3f offset = extended.interpolate(this.mc.timer.renderPartialTicks).sub(new Vec3f(extended.getPosition().getX(),extended.getPosition().getY(),extended.getPosition().getZ())).add(0.0, 0.1, 0.0);
        if (entity.isInvisible()) {
            return;
        }
        AxisAlignedBB bb = entity.getEntityBoundingBox().offset(offset.getX() - RenderManager.renderPosX, offset.getY() - RenderManager.renderPosY, offset.getZ() - RenderManager.renderPosZ);
        this.points.get(0).setX(bb.minX).setY(bb.minY).setZ(bb.minZ);
        this.points.get(1).setX(bb.maxX).setY(bb.minY).setZ(bb.minZ);
        this.points.get(2).setX(bb.maxX).setY(bb.minY).setZ(bb.maxZ);
        this.points.get(3).setX(bb.minX).setY(bb.minY).setZ(bb.maxZ);
        this.points.get(4).setX(bb.minX).setY(bb.maxY).setZ(bb.minZ);
        this.points.get(5).setX(bb.maxX).setY(bb.maxY).setZ(bb.minZ);
        this.points.get(6).setX(bb.maxX).setY(bb.maxY).setZ(bb.maxZ);
        this.points.get(7).setX(bb.minX).setY(bb.maxY).setZ(bb.maxZ);
        float left = Float.MAX_VALUE;
        float right = 0.0f;
        float top = Float.MAX_VALUE;
        float bottom = 0.0f;
        for (Vec3f point : this.points) {
            Vec3f screen = point.toScreen();
            if (screen.getZ() < 0.0 || screen.getZ() >= 1.0) continue;
            if (screen.getX() < (double)left) {
                left = (float)screen.getX();
            }
            if (screen.getY() < (double)top) {
                top = (float)screen.getY();
            }
            if (screen.getX() > (double)right) {
                right = (float)screen.getX();
            }
            if (screen.getY() <= (double)bottom) continue;
            bottom = (float)screen.getY();
        }
        if (bottom <= 1.0f && right <= 1.0f) {
            return;
        }
        this.box(left, top, right, bottom);
        this.name(entity, left, top, right, bottom);
        if (!(entity instanceof EntityLivingBase)) {
            return;
        }
        EntityLivingBase living = (EntityLivingBase)entity;
        this.health(living, left, top, right, bottom);
    }

    private void box(float left, float top, float right, float bottom) {
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)0.5);
        RenderUtil.drawLine(left, top, right, top, 2.0f);
        RenderUtil.drawLine(left, bottom, right, bottom, 2.0f);
        RenderUtil.drawLine(left, top, left, bottom, 2.0f);
        RenderUtil.drawLine(right, top, right, bottom, 2.0f);
        RenderUtil.drawLine(left + 1.0f, top + 1.0f, right - 1.0f, top + 1.0f, 1.0f);
        RenderUtil.drawLine(left + 1.0f, bottom - 1.0f, right - 1.0f, bottom - 1.0f, 1.0f);
        RenderUtil.drawLine(left + 1.0f, top + 1.0f, left + 1.0f, bottom - 1.0f, 1.0f);
        RenderUtil.drawLine(right - 1.0f, top + 1.0f, right - 1.0f, bottom - 1.0f, 1.0f);
        RenderUtil.drawLine(left - 1.0f, top - 1.0f, right + 1.0f, top - 1.0f, 1.0f);
        RenderUtil.drawLine(left - 1.0f, bottom + 1.0f, right + 1.0f, bottom + 1.0f, 1.0f);
        RenderUtil.drawLine(left - 1.0f, top + 1.0f, left - 1.0f, bottom + 1.0f, 1.0f);
        RenderUtil.drawLine(right + 1.0f, top - 1.0f, right + 1.0f, bottom + 1.0f, 1.0f);
    }

    private void name(Entity entity, float left, float top, float right, float bottom) {
        this.mc.fontRendererObj.drawCenteredString(FriendManager.isFriend(entity.getName()) ? "\u00a7b" + FriendManager.getAlias(entity.getName()) : entity.getName(), (int)(left + right) / 2, (int)(top - (float)this.mc.fontRendererObj.FONT_HEIGHT - 2.0f + 1.0f), -1);
        if (((EntityPlayer)entity).getCurrentEquippedItem() != null) {
            String stack = ((EntityPlayer)entity).getCurrentEquippedItem().getDisplayName();
            this.mc.fontRendererObj.drawCenteredString(stack, (int)(left + right) / 2, (int)bottom, -1);
        }
    }

    private void health(EntityLivingBase entity, float left, float top, float right, float bottom) {
        float height = bottom - top;
        float currentHealth = entity.getHealth();
        float maxHealth = entity.getMaxHealth();
        float healthPercent = currentHealth / maxHealth;
        GLUtils.glColor(this.getHealthColor(entity));
        RenderUtil.drawLine(left - 5.0f, top + height * (1.0f - healthPercent) + 1.0f, left - 5.0f, bottom, 2.0f);
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0f, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0f, 1.0f, 1.0f) | -16777216;
    }


}

