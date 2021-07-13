package tomorrow.tomo.utils.irc.packets.serverside;

import tomorrow.tomo.utils.irc.packets.IRCPacket;
import tomorrow.tomo.utils.irc.packets.IRCType;

public class ServerHeartNeededPacket extends IRCPacket {
    public ServerHeartNeededPacket(long time, String content) {
        super(time, content, IRCType.HEART);
    }
}
