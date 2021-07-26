package tomorrow.tomo.mods.modules.render.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import tomorrow.tomo.Client;
import tomorrow.tomo.guis.font.CFontRenderer;
import tomorrow.tomo.guis.font.FontLoaders;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.modules.ClientSettings;
import tomorrow.tomo.mods.modules.render.HUD;
import tomorrow.tomo.utils.cheats.player.Helper;
import tomorrow.tomo.utils.cheats.world.TimerUtil;
import tomorrow.tomo.utils.render.RenderUtil;

import java.awt.*;

public class ArrayList {
    private int rainbowTick;
    private int rainbowTick2;
    private TimerUtil timer = new TimerUtil();

    public void drawObject() {
        if (!HUD.arraylist.getValue()) {
            return;
        }
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float x1 = sr.getScaledWidth(), y1 = 0;
        CFontRenderer font = FontLoaders.roboto16;
        if (Helper.mc.thePlayer.ticksExisted % 20 == 0) {
            ModuleManager.modules.sort(((o1, o2) -> font.getStringWidth(o2.getSuffix().isEmpty() ? o2.getName() : String.format("%s %s", o2.getName(), o2.getSuffix())) - font.getStringWidth(o1.getSuffix().isEmpty() ? o1.getName() : String.format("%s %s", o1.getName(), o1.getSuffix()))));
        }

        rainbowTick = 0;
        rainbowTick2 = 0;

        java.util.ArrayList<Module> mods = new java.util.ArrayList<>();
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
        int i = 0;
        for (Module mod : mods) {
            if (!mod.isEnabled())
                return;
            if (++rainbowTick2 > 50) {
                rainbowTick2 = 0;
            }
            Color arrayRainbow2 = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
            if (HUD.colorMode.getValue().equals("Rainbow")) {
                arrayRainbow2 = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, ClientSettings.saturation.getValue().floatValue(), ClientSettings.brightness.getValue().floatValue()));
            } else if (HUD.colorMode.getValue().equals("ColoredRainbow")) {
                Color temp = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 0.5f, 1));
                arrayRainbow2 = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue(), temp.getRed());
            } else if (HUD.colorMode.getValue().equals("Color")) {
                arrayRainbow2 = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
            }

            if (i + 1 <= mods.size() - 1) {
                Module m2 = mods.get(i + 1);
                RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY) + 11, (int) mod.animX - 9 + font.getStringWidth(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix()) - font.getStringWidth(m2.getName() + (m2.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + m2.getSuffix()), (int) mod.animY + 12, arrayRainbow2.getRGB());
            } else if (i == mods.size() - 1) {
                RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY) + 11, x1, (int) mod.animY + 12, arrayRainbow2.getRGB());
            }
            RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY), ((int) mod.animX - 9), (int) mod.animY + 11, arrayRainbow2.getRGB());
            RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY), (x1), (int) mod.animY + 12, new Color(0, 0, 0, 100).getRGB());
//            Gui.drawRect((x1) - 1f, mod.animY, (x1), mod.animY + 12, arrayRainbow2.getRGB());
            font.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix(), mod.animX - 8, mod.animY + 3, arrayRainbow2.getRGB());
            arrayListY += 12f;
            i++;
        }
        if (rainbowTick++ > 50) {
            rainbowTick = 0;
        }
    }
}
