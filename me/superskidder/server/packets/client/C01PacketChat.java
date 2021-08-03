package me.superskidder.server.packets.client;

import me.superskidder.server.packets.ClientPacket;
import me.superskidder.server.packets.Packet;
import me.superskidder.server.packets.PacketType;
import me.superskidder.server.util.json.JsonUtil;

public class C01PacketChat extends ClientPacket implements Packet{
    public C01PacketChat(String chat) {
        content = chat;
        packetType = PacketType.C01;
    }

    @Override
    public String getJson() {
        return JsonUtil.toJson(this);
    }

    @Override
    public ClientPacket parsePacket(String j) {
        return (ClientPacket) JsonUtil.parseJson(j, this.getClass());
    }
}
