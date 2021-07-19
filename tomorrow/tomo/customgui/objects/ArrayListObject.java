package tomorrow.tomo.customgui.objects;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import tomorrow.tomo.Client;
import tomorrow.tomo.customgui.GuiObject;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.guis.font.CFontRenderer;
import tomorrow.tomo.guis.font.FontLoaders;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.modules.ClientSettings;
import tomorrow.tomo.mods.modules.render.HUD;
import tomorrow.tomo.utils.cheats.player.Helper;
import tomorrow.tomo.utils.cheats.world.TimerUtil;
import tomorrow.tomo.utils.math.AnimationUtils;
import tomorrow.tomo.utils.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

public class ArrayListObject extends GuiObject {

    public Mode mode = new Mode("mod", "mod", mod.values(), mod.Flux);
    public TimerUtil timer = new TimerUtil();

    public ArrayListObject(String name, float x, float y) {
        super(name, x, y);
    }

    int rainbowTick = 0;
    int rainbowTick2 = 0;

    enum mod {
        Flux,
        Other
    }


    @Override
    public void drawObject() {
        if (!HUD.arraylist.getValue()) {
            return;
        }
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float x1 = sr.getScaledWidth() * x / 100, y1 = sr.getScaledHeight() * y / 100f;
        CFontRenderer font = FontLoaders.roboto16;
        if (Helper.mc.thePlayer.ticksExisted % 20 == 0) {
            ModuleManager.modules.sort(((o1, o2) -> font.getStringWidth(o2.getSuffix().isEmpty() ? o2.getName() : String.format("%s %s", o2.getName(), o2.getSuffix())) - font.getStringWidth(o1.getSuffix().isEmpty() ? o1.getName() : String.format("%s %s", o1.getName(), o1.getSuffix()))));
        }
        if (mode.getValue().toString().equals(mod.Flux.name())) {

            rainbowTick = 0;
            rainbowTick2 = 0;
//        Color rainbow = new Color(Color.HSBtoRGB((float) ((double) this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
//            Color arrayRainbow = new Color(Color.HSBtoRGB((float) ((double) rainbowTick2 / 50.0 + Math.sin((double) rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
//            Color rainbow = new Color(255, 255, 255);


            ArrayList<Module> mods = new ArrayList<>();
            for (Module m : ModuleManager.modules) {
                if (m.isEnabled()) {
                    mods.add(m);
                } else {
                    m.animX = x1;
                }
            }

            float ys = y1;
            if (timer.delay(10)) {
                for (Module mod : mods) {
                    mod.animY = mod.animationUtils.animate(ys, mod.animY, 0.15f);
                    mod.animX = mod.animationUtils2.animate(x1 - (font.getStringWidth(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix()) - 4), mod.animX, 0.15f);
                    ys += 12;
                }
                timer.reset();
            }

            float arrayListY = y1;
            for (Module mod : mods) {
//                ModuleManager.modules.sort(((o1, o2) -> font.getStringWidth(o2.getSuffix().isEmpty() ? o2.getName() : String.format("%s %s", o2.getName(), o2.getSuffix())) - font.getStringWidth(o1.getSuffix().isEmpty() ? o1.getName() : String.format("%s %s", o1.getName(), o1.getSuffix()))));
                if (!mod.isEnabled())
                    return;
//                if (mod.modName.equals("HUD"))
//                    return;

                if (++rainbowTick2 > 50) {
                    rainbowTick2 = 0;
                }
                Color arrayRainbow2 = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
                if (HUD.colorMode.getValue().equals(HUD.colorMods.Rainbow)) {
                    arrayRainbow2 = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, ClientSettings.saturation.getValue().floatValue(), ClientSettings.brightness.getValue().floatValue()));
                } else if (HUD.colorMode.getValue().equals(HUD.colorMods.ColoredRainbow)) {
                    Color temp = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 0.5f, 1));
                    arrayRainbow2 = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue(), temp.getRed());
                } else if (HUD.colorMode.getValue().equals(HUD.colorMods.Color)) {
                    arrayRainbow2 = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
                }
//            Color arrayRainbow2 = new Color(255, 255, 255);
                RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY), (x1), (int) mod.animY + 12, new Color(0, 0, 0, 100).getRGB());
                Gui.drawRect((x1) - 1f, mod.animY, (x1), mod.animY + 12, arrayRainbow2.getRGB());
                font.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix(), mod.animX - 8, mod.animY + 3, arrayRainbow2.getRGB());
                if (width > -(font.getStringWidth(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix()) + 8)) {
                    width = -(font.getStringWidth(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix()) + 8);
                }

                arrayListY += 12f;
            }

            super.height = (arrayListY - y1);
            if (rainbowTick++ > 50) {
                rainbowTick = 0;
            }

        } else if (mode.isValid(mod.Other.name())) {


        }


    }
}
