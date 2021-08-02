package me.superskidder.server.packets.client;

import me.superskidder.server.data.User;
import me.superskidder.server.packets.ClientPacket;
import me.superskidder.server.packets.Packet;
import me.superskidder.server.packets.PacketType;
import me.superskidder.server.util.json.JsonUtil;

public class C00PacketConnect extends ClientPacket implements Packet {

    public C00PacketConnect(User user) {
        this.user = user;
        this.packetType = PacketType.C00;
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
