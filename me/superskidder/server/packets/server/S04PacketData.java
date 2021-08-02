package me.superskidder.server.packets.server;

import me.superskidder.server.packets.ClientPacket;
import me.superskidder.server.packets.Packet;
import me.superskidder.server.util.json.JsonUtil;

public class S04PacketData implements Packet {
    @Override
    public String getJson() {
        return JsonUtil.toJson(this);
    }

    @Override
    public ClientPacket parsePacket(String j) {
        return (ClientPacket) JsonUtil.parseJson(j,this.getClass());
    }
}
