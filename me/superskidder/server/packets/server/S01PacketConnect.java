package me.superskidder.server.packets.server;

import me.superskidder.server.packets.ClientPacket;
import me.superskidder.server.packets.Packet;
import me.superskidder.server.packets.PacketType;
import me.superskidder.server.packets.ServerPacket;
import me.superskidder.server.util.json.JsonUtil;

public class S01PacketConnect extends ServerPacket implements Packet {
    public S01PacketConnect(String connected) {
        this.content = connected;
        this.packetType = PacketType.S01;
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
