package me.superskidder.server.packets;


import me.superskidder.server.util.json.JsonUtil;

public class PacketUtil {

    public static String getClientJson(ClientPacket p) {
        return JsonUtil.toJson(p);
    }

    public static String getServerJson(ServerPacket p) {
        return JsonUtil.toJson(p);
    }


    public static ServerPacket parsePacketServer(String j, Class cls) {
        return (ServerPacket) JsonUtil.parseJson(j, cls);
    }

    public static ClientPacket parsePacketClient(String j, Class cls) {
        return (ClientPacket) JsonUtil.parseJson(j, cls);
    }
}
