package tomorrow.tomo.managers.config;

import tomorrow.tomo.Client;
import tomorrow.tomo.mods.Module;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    public List<ModuleConfig> settings = new ArrayList<>();


    public void init(){
        for (Module m : Client.instance.getModuleManager().modules){
            settings.add(new ModuleConfig(m.getName(),m.isEnabled(),m.getValues(),m.getKey()));
        }
    }
}
