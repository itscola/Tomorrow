package me.superskidder.server.packets;

public interface Packet {
    String getJson();

    ClientPacket parsePacket(String j);

}
