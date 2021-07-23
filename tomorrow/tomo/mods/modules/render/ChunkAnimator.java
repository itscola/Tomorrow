package tomorrow.tomo.mods.modules.render;

import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;


public class ChunkAnimator extends Module {


    public Mode mod = new Mode("Mode", "Mode", new String[]{"Below", "Above", "HorizonAbove", "slide", "slide2"}, "Below");

    public ChunkAnimator() {
        super("ChunkAnimator", ModuleType.Render);
        addValues(mod);
    }


    public int getMode() {
        switch (mod.getModeAsString()) {
            case "Below":
                return 0;
            case "Above":
                return 1;
            case "HorizonAbove":
                return 2;
            case "slide":
                return 3;
            case "slide2":
                return 4;
        }

        return 0;
    }

}
