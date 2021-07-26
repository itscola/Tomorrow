/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import tomorrow.tomo.Client;
import tomorrow.tomo.customgui.objects.ArrayListObject;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.rendering.EventRender2D;
import tomorrow.tomo.event.events.world.EventPostUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.guis.font.CFontRenderer;
import tomorrow.tomo.guis.font.FontLoaders;
import tomorrow.tomo.guis.notification.NotificationsManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.mods.modules.render.UI.TabUI;
import tomorrow.tomo.utils.math.AnimationUtils;
import tomorrow.tomo.utils.render.RenderUtil;

import java.awt.*;


public class HUD
        extends Module {
    public TabUI tabui;
    public static Option<Boolean> tabGui = new Option<Boolean>("TabGUI", "TabGUI", true);
    public static Option<Boolean> notification = new Option<Boolean>("Notification", "Notification", true);
    public static Option<Boolean> arraylist = new Option<Boolean>("Arraylist", "Arraylist", true);
    public static Mode colorMode = new Mode("ArrayListColor", "ArrayListColor", new String[]{"ColoredRainbow", "Color", "Rainbow"}, "ColoredRainbow");
    public Mode mod = new Mode("Mode", "Mode", new String[]{"Flux", "OverWatch"}, "Flux");

    private AnimationUtils animationUtils = new AnimationUtils();

    public HUD() {
        super("HUD", ModuleType.Render);
        this.setEnabled(true);
        this.addValues(this.tabGui, this.notification, this.arraylist, this.mod, colorMode);
    }

    int rainbowTick = 0;
    int rainbowTick2 = 0;

    @Override
    public void onEnable() {
        if (Client.instance.customgui.objects.size() == 0) {
            Client.instance.customgui.addObject(new ArrayListObject("ArrayList", 100, 0));
        }
    }

    @Override
    public void onDisable() {
    }

    float x;

    @EventHandler
    public void renderHud(EventRender2D event) {
        Client.instance.customgui.drawGuiPre();
        ScaledResolution sr = new ScaledResolution(mc);

        if (((boolean) notification.getValue())) {
            NotificationsManager.renderNotifications();
            NotificationsManager.update();
        }
        Color rainbow = new Color(Color.HSBtoRGB((float) ((double) this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) rainbowTick / 50.0)) % 1.0f, 0.5f, 1.0f));

        if (!mc.gameSettings.showDebugInfo) {
//            FontLoaders.arial24.drawStringWithShadow(Client.CLIENT_NAME + " " +ChatFormatting.GRAY + Client.VERSION, 4, 4, rainbow.getRGB());
//            RenderUtil.drawCustomImage(10, 2, 41, 41, new ResourceLocation("client/hudlogo.png"), new Color(255, 255, 255).getRGB());

            RenderUtil.drawCustomImageAlpha(10, 2, 41, 41, new ResourceLocation("client/hudlogo.png"), new Color(255, 255, 255).getRGB(), rainbow.getBlue());
            TabUI.height = 42;
        }

        if (mod.getValue().equals("Flux")) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            double moveSpeed = Math.sqrt(xDist * xDist + zDist * zDist) * 20;
            String text = (Object) ((Object) EnumChatFormatting.GRAY) + "X" + (Object) ((Object) EnumChatFormatting.WHITE) + ": " + MathHelper.floor_double(mc.thePlayer.posX) + " " + (Object) ((Object) EnumChatFormatting.GRAY) + "Y" + (Object) ((Object) EnumChatFormatting.WHITE) + ": " + MathHelper.floor_double(mc.thePlayer.posY) + " " + (Object) ((Object) EnumChatFormatting.GRAY) + "Z" + (Object) ((Object) EnumChatFormatting.WHITE) + ": " + MathHelper.floor_double(mc.thePlayer.posZ) + "  " + Math.round(moveSpeed) + " \2477b/s\247r";
            Client.fontLoaders.msFont18.drawStringWithShadow(text, 4.0F, new ScaledResolution(mc).getScaledHeight() - 10, new Color(11, 12, 17).getRGB());
            drawPotionStatus(sr);
        } else if (mod.getValue().equals("OverWatch")) {
            GlStateManager.rotate(-15, 1, 1, 1);
            FontLoaders.arial24.drawString((int) mc.thePlayer.getHealth() + "/" + (int) mc.thePlayer.getMaxHealth(), 10, sr.getScaledHeight() - 75, -1);
            for (int i = 0; i < 20; i++) {
                if (i > ((int) mc.thePlayer.getHealth())) {
                    RenderUtil.drawRect(i * 9, sr.getScaledHeight() - 60, (i % 2 == 0 ? 9 : 8) + i * 9, sr.getScaledHeight() - 43, new Color(255, 255, 255, 170));
                }
            }

            for (int i = 0; i < 20; i++) {
                if (i <= ((int) mc.thePlayer.getHealth())) {
                    RenderUtil.drawRect(i * 9, sr.getScaledHeight() - 60, (i % 2 == 0 ? 9 : 8) + i * 9, sr.getScaledHeight() - 43, new Color(255, 255, 255, 230));
                }
            }

            RenderUtil.drawRect(0, sr.getScaledHeight() - 40, 5 + 7 * 18, sr.getScaledHeight() - 38.5f, new Color(255, 255, 255, 180));
            RenderUtil.drawRect(0, sr.getScaledHeight() - 40, (5 + 7 * 18) * ((float) mc.thePlayer.getTotalArmorValue() / 20f), sr.getScaledHeight() - 38.5f, new Color(71, 142, 255, 255));


            GlStateManager.rotate(15, 1, 1, 1);

        }

        Client.instance.customgui.drawGuiPost();
    }

    @EventHandler
    public void onUpdate(EventPostUpdate e) {
        if (rainbowTick++ > 50) {
            rainbowTick = 0;
        }


    }

    private void drawPotionStatus(ScaledResolution sr) {
        CFontRenderer font = FontLoaders.arial16;
        int y = 0;
        for (PotionEffect effect : this.mc.thePlayer.getActivePotionEffects()) {
            int ychat;
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String PType = I18n.format(potion.getName(), new Object[0]);
            switch (effect.getAmplifier()) {
                case 1: {
                    PType = String.valueOf(PType) + " II";
                    break;
                }
                case 2: {
                    PType = String.valueOf(PType) + " III";
                    break;
                }
                case 3: {
                    PType = String.valueOf(PType) + " IV";
                    break;
                }
            }
            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                PType = String.valueOf(PType) + "\u00a77:\u00a76 " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = String.valueOf(PType) + "\u00a77:\u00a7c " + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                PType = String.valueOf(PType) + "\u00a77:\u00a77 " + Potion.getDurationString(effect);
            }
            font.drawStringWithShadow(PType, sr.getScaledWidth() - font.getStringWidth(PType) - 2, sr.getScaledHeight() - font.getHeight() + y, potion.getLiquidColor());
            y -= 12;
        }
    }

}

