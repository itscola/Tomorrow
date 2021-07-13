package tomorrow.tomo.utils.irc.packets.clientside;

import tomorrow.tomo.utils.irc.packets.IRCPacket;
import tomorrow.tomo.utils.irc.packets.IRCType;

public class ClientChatPacket extends IRCPacket {
    public ClientChatPacket(long time,String content){
        super(time,content, IRCType.CHAT);
    }
}
