package tomorrow.tomo.commands.commands;

import tomorrow.tomo.commands.Command;
import tomorrow.tomo.utils.irc.Client;
import tomorrow.tomo.utils.irc.packets.clientside.ClientChatPacket;

public class IRC extends Command {
    public IRC() {
        super("irc", new String[]{"i"}, "<content>", "chat with other hacker that using tomorrow");
    }

    @Override
    public String execute(String[] args) {
        String t = "";
        for (String s : args) {
            t = t + s + " ";
        }


        Client.sendChatMessage(new ClientChatPacket(System.currentTimeMillis(), t));
        return null;
    }
}
