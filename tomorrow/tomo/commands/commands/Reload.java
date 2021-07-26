package tomorrow.tomo.commands.commands;

import java.util.Iterator;

import tomorrow.tomo.Client;
import tomorrow.tomo.commands.Command;
import tomorrow.tomo.customgui.CustomGuiManager;
import tomorrow.tomo.event.EventBus;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.utils.cheats.player.Helper;

public class Reload extends Command {
    public Reload() {
        super("reload", new String[]{"reloadmods"}, "", "reload all modules(config may lose or crash)");
    }

    @Override
    public String execute(String[] var1) {
    	// unregister all the mods
    	for (Module mod : ModuleManager.enabledModules) {
			EventBus.getInstance().unregister(mod);
		}
    	// clear the modules lists
        ModuleManager.modules.clear();
        CustomGuiManager.objects.clear();
        ModuleManager.enabledModules.clear();
        // unregister ModuleManager::onKey....
        EventBus.getInstance().unregister(Client.instance.getModuleManager());
        
        // reload modules
        Client.instance.getModuleManager().init();
        Helper.sendMessage("Reloaded");
        return null;
    }
}
