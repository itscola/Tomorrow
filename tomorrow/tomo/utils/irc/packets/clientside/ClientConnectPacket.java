package tomorrow.tomo.utils.irc.packets.clientside;

import tomorrow.tomo.utils.irc.packets.IRCPacket;
import tomorrow.tomo.utils.irc.packets.IRCType;

public class ClientConnectPacket extends IRCPacket {

    public String username = "";
    public String gameID = "";

    public ClientConnectPacket(long time, String content,String username,String gameID) {
        super(time, content, IRCType.CONNECT);
        this.username = username;
        this.gameID = gameID;
    }
}
