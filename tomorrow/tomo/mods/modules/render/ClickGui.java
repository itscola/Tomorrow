package tomorrow.tomo.mods.modules.render;

import org.lwjgl.input.Keyboard;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.guis.clickgui2.TomoClickGui;
import tomorrow.tomo.guis.clickgui2.theme.Theme;
import tomorrow.tomo.guis.material.Main;
import tomorrow.tomo.guis.material.themes.MainWhite;
import tomorrow.tomo.mods.Mod;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

@Mod(name = "ClickGui", description = ".", type = ModuleType.Render)

public class ClickGui extends Module {
    public Mode mode = new Mode("Mode", "Mode", Modes.values(), Modes.Material);
    public Mode color = new Mode("Theme", "Theme", themes.values(), themes.Dark);

    enum Modes {
        VapuLite,
        Material
    }

    enum themes {
        White,
        Dark,
    }

    public ClickGui() {
        super("ClickGui", ModuleType.Render);
        this.setKey(Keyboard.KEY_RSHIFT);
        addValues(color);
    }


    @Override
    public void onEnable() {
//        mc.displayGuiScreen(new tomorrow.client.guis.clickgui.ClickGui());

        if (color.getValue().equals(themes.Dark)) {
            if(mode.getValue().equals(Modes.VapuLite)) {
                TomoClickGui.theme.setDark();
                mc.displayGuiScreen(new TomoClickGui());
            }else if(mode.getValue().equals(Modes.Material)) {
                // TODO: 2021/7/19 dark mode 
                mc.displayGuiScreen(new MainWhite());
            }
        } else if (color.getValue().equals(themes.White)) {
            if(mode.getValue().equals(Modes.VapuLite)) {
                TomoClickGui.theme.setWhite();
                mc.displayGuiScreen(new TomoClickGui());
            }else if(mode.getValue().equals(Modes.Material)) {
                mc.displayGuiScreen(new MainWhite());
            }
        }

        this.setEnabled(false);
    }
}
