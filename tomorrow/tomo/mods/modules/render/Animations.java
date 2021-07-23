package tomorrow.tomo.mods.modules.render;

import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

public class Animations extends Module {

    public static Mode mode = new Mode("Mode", "Mode", new String[]{"NONE", "1.7", "Swang", "Swank"}, "1.7");
    public static Numbers<Number> speed = new Numbers<>("Speed", 1, 1, 15, 1);

    public Animations() {
        super("Animations", ModuleType.Render);
        addValues(mode, speed);
    }
}
