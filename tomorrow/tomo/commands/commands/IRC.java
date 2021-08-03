package tomorrow.tomo.commands.commands;

import me.superskidder.server.packets.client.C01PacketChat;
import me.superskidder.server.packets.client.C02PacketCommand;
import tomorrow.tomo.commands.Command;
import tomorrow.tomo.managers.IRC.IRCClient;

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
        if (t.startsWith("/")) {
            IRCClient.addPacket(IRCClient.writer, new C02PacketCommand(t));
        } else {
            IRCClient.addPacket(IRCClient.writer, new C01PacketChat(t));
        }
        return null;
    }
}
