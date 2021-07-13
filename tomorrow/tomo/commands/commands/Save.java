package tomorrow.tomo.commands.commands;

import tomorrow.tomo.commands.Command;
import tomorrow.tomo.utils.cheats.player.Helper;
import tomorrow.tomo.utils.cheats.misc.JsonUtil;

public class Save extends Command {
    public Save() {
        super("save", new String[]{}, "", "save client config.");
    }

    @Override
    public String execute(String[] var1) {
        if (var1.length < 1){
            Helper.sendMessage("�÷�: .save <load/save>");
        }

        if(var1[0].equals("load")){
            JsonUtil.load();
            Helper.sendMessage("Loaded!");

        }else {
            JsonUtil.saveConfig();
            Helper.sendMessage("Saved!");
        }
        return null;
    }
}
