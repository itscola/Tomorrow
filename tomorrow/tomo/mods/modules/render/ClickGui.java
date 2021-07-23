package tomorrow.tomo.mods.modules.render;

import org.lwjgl.input.Keyboard;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.guis.clickgui2.TomoClickGui;
import tomorrow.tomo.guis.material.themes.Classic;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;


public class ClickGui extends Module {
    public Mode mode = new Mode("Mode", "Mode", new String[]{"VapeLite", "Material"}, "Material");
    public Mode color = new Mode("Theme", "Theme", new String[]{"White", "Dark"}, "White", mode.getValue().equals("VapeLite"));


    public ClickGui() {
        super("ClickGui", ModuleType.Render);
        this.setKey(Keyboard.KEY_RSHIFT);
        addValues(color, mode);
    }


    @Override
    public void onEnable() {
//        mc.displayGuiScreen(new tomorrow.client.guis.clickgui.ClickGui());

        if (color.getValue().equals("Dark")) {
            if (mode.getValue().equals("VapuLite")) {
                TomoClickGui.theme.setDark();
                mc.displayGuiScreen(new TomoClickGui());
            } else if (mode.getValue().equals("Material")) {
                // TODO: 2021/7/19 dark mode 
                mc.displayGuiScreen(new Classic());
            }
        } else if (color.getValue().equals("White")) {
            if (mode.getValue().equals("VapeLite")) {
                TomoClickGui.theme.setWhite();
                mc.displayGuiScreen(new TomoClickGui());
            } else if (mode.getValue().equals("Material")) {
                mc.displayGuiScreen(new Classic());
            }
        }

        this.setEnabled(false);
    }
}
