package tomorrow.tomo.managers.config;

import tomorrow.tomo.event.value.Value;

import java.util.ArrayList;
import java.util.List;

public class ModuleConfig {
    public String name;
    public Boolean enable;
    public List<Value> values = new ArrayList<>();
    public int bind;

    public ModuleConfig(String name, boolean enable, List<Value> values,int bind){
        this.name = name;
        this.enable = enable;
        this.values = values;
        this.bind = bind;
    }
}
