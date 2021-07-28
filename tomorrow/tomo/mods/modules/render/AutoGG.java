package tomorrow.tomo.mods.modules.render;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.misc.EventChat;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.guis.notification.Notification;
import tomorrow.tomo.guis.notification.NotificationsManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

public class AutoGG extends Module {
    public Option gg = new Option("GG", "GG", true);
    public Option autoplay = new Option("Autoplay", "Autoplay", true);

    public AutoGG() {
        super("AutoGG", ModuleType.Misc);
        addValues(gg, autoplay);
    }

    @EventHandler
    public void onChat(EventChat c) {
        if (c.getMessage().contains("Winner - ") || c.getMessage().contains("ʤ����")) {
            NotificationsManager.addNotification(new Notification("The game will start in 3 seconds.", Notification.Type.Alert,3));
            mc.thePlayer.sendChatMessage("/play solo_normal");
        }
    }

}
