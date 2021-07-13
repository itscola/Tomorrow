package tomorrow.tomo.utils.irc.packets.serverside;

import tomorrow.tomo.utils.irc.packets.IRCPacket;
import tomorrow.tomo.utils.irc.packets.IRCType;

public class ServerChatPacket extends IRCPacket {
    public ServerChatPacket(long time, String content){
        super(time,content, IRCType.CHAT);
    }
}
