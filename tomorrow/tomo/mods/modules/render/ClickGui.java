package tomorrow.tomo.mods.modules.render;

import org.lwjgl.input.Keyboard;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.guis.clickgui2.TomoClickGui;
import tomorrow.tomo.guis.clickgui2.theme.Theme;
import tomorrow.tomo.mods.Mod;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

@Mod(name = "ClickGui", description = ".", type = ModuleType.Render)

public class ClickGui extends Module {
    public Mode color = new Mode("Theme", "Theme", themes.values(), themes.Dark);

    enum themes {
        White,
        Dark
    }

    public ClickGui() {
        super("ClickGui", ModuleType.Render);
        this.setKey(Keyboard.KEY_RSHIFT);
        addValues(color);
    }


    @Override
    public void onEnable() {
//        mc.displayGuiScreen(new tomorrow.client.guis.clickgui.ClickGui());

        if(color.getValue().equals(themes.Dark)){
            TomoClickGui.theme.setDark();
        }else if(color.getValue().equals(themes.White)){
            TomoClickGui.theme.setWhite();
        }
        mc.displayGuiScreen(new TomoClickGui());

        this.setEnabled(false);
    }
}
