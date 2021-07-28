package tomorrow.tomo.mods.modules;

import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

// TODO: 2021/7/5 把这个module的类型改为无类型并显示在clickgui settings界面中
public class ClientSettings extends Module {
    public static Numbers<Number> r = new Numbers<>("R", "R", 0, 0, 255, 1);
    public static Numbers<Number> g = new Numbers<>("G", "G", 120, 0, 255, 1);
    public static Numbers<Number> b = new Numbers<>("B", "B", 255, 0, 255, 1);
    public static Numbers<Number> saturation = new Numbers<>("saturation", "saturation", 0.5, 0.1, 1, 0.01);
    public static Numbers<Number> brightness = new Numbers<>("brightness", "brightness", 0.5, 0.1, 1, 0.01);
    public static Numbers<Number> particlesLimit = new Numbers<>("particlesLimit", "particlesLimit", 300, 50, 3000, 1);


    public ClientSettings() {
        super("ClientSettings", ModuleType.Misc);
        addValues(r, g, b, saturation, brightness,particlesLimit);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.setEnabled(false);
    }
}
