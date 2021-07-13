package tomorrow.tomo.utils.irc.packets.clientside;

import tomorrow.tomo.utils.irc.packets.IRCPacket;
import tomorrow.tomo.utils.irc.packets.IRCType;

public class ClientHeartPacket extends IRCPacket {
    public ClientHeartPacket(long time, String content) {
        super(time, content, IRCType.HEART);
    }
}
