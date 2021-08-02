package me.superskidder.server.packets.client;

import me.superskidder.server.packets.ClientPacket;
import me.superskidder.server.packets.Packet;
import me.superskidder.server.packets.PacketType;
import me.superskidder.server.util.json.JsonUtil;

public class C02PacketCommand extends ClientPacket implements Packet {

    public C02PacketCommand(String command){
        this.packetType = PacketType.C02;
        this.content = command;
    }

    @Override
    public String getJson() {
        return JsonUtil.toJson(this);
    }

    @Override
    public ClientPacket parsePacket(String j) {
        return (ClientPacket) JsonUtil.parseJson(j,this.getClass());
    }
}
