package me.gb2022.simpnet.server;

import me.gb2022.simpnet.packet.Packet;

@FunctionalInterface
public interface ServerPacketHandler<P extends Packet>{
    void handle(P packet, ServerContext context);
}
