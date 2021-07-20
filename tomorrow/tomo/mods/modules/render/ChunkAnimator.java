package tomorrow.tomo.mods.modules.render;

import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;


public class ChunkAnimator extends Module {


    public Mode mod = new Mode("Mode", "Mode", Mod.values(), Mod.Below);

    public ChunkAnimator() {
        super("ChunkAnimator", ModuleType.Render);
        addValues(mod);
    }


    public int getMode(){
        if(mod.getValue() == Mod.Below){
            return 0;
        }else if(mod.getValue() == Mod.Above){
            return 1;
        }else if(mod.getValue() == Mod.HorizonAbove){
            return 2;
        }else if(mod.getValue() == Mod.slide){
            return 3;
        }else if(mod.getValue() == Mod.same){
            return 4;
        }
        return 0;
    }

    enum Mod {
        Below,
        Above,
        HorizonAbove,
        slide,
        same
    }
}
