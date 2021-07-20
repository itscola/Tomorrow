package tomorrow.tomo.mods.modules.misc;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPostUpdate;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.cheats.player.Helper;
import tomorrow.tomo.utils.irc.Client;
import tomorrow.tomo.utils.irc.User;

import javax.swing.*;

public class IRC extends Module {
    public IRC(){
        super("IRC", ModuleType.Misc);
    }
    @Override
    public void onEnable() {
        super.onEnable();
        Client.user = new User("","","","");
        System.out.println("Connecting irc server");
        tomorrow.tomo.utils.irc.Client.connect("SuperSkidder", "IDK");
    }

    @EventHandler
    public void onUpdate(EventPostUpdate e) {
        Client.user.GameID = mc.thePlayer.getName();
//        Client.user.head = "http:://baidu.com/B.png";
    }
}
