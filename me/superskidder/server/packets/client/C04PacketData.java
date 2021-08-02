package me.superskidder.server.packets.client;

import me.superskidder.server.data.User;
import me.superskidder.server.packets.ClientPacket;
import me.superskidder.server.packets.Packet;
import me.superskidder.server.packets.PacketType;
import me.superskidder.server.util.json.JsonUtil;

public class C04PacketData extends ClientPacket implements Packet {

    public C04PacketData(User u, String con) {
        this.content = con;
        this.user = u;
        this.packetType = PacketType.C04;
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
