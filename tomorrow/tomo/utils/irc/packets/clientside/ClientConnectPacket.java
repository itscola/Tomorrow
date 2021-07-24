package tomorrow.tomo.utils.irc.packets.clientside;

import tomorrow.tomo.utils.irc.packets.IRCPacket;
import tomorrow.tomo.utils.irc.packets.IRCType;

public class ClientConnectPacket extends IRCPacket {

    public String username = "";
    public String password = "";
    public String hwid = "";

    public ClientConnectPacket(long time, String content,String username,String password,String hwid) {
        super(time, content, IRCType.CONNECT);
        this.username = username;
        this.password = password;
        this.hwid = hwid;
    }
}
