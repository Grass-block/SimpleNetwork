package me.gb2022.simpnet.client;

import me.gb2022.simpnet.packet.Packet;

@FunctionalInterface
public interface ClientPacketHandler<P extends Packet>{
    void handle(P packet, ClientContext context);
}
