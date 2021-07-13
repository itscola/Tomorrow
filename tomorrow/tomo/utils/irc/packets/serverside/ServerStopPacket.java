package tomorrow.tomo.utils.irc.packets.serverside;

import tomorrow.tomo.utils.irc.packets.IRCPacket;
import tomorrow.tomo.utils.irc.packets.IRCType;

public class ServerStopPacket extends IRCPacket {
    public ServerStopPacket(long time, String content) {
        super(time, content, IRCType.STOP);
    }
}
