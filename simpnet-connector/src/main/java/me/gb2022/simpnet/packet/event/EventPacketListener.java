package me.gb2022.simpnet.packet.event;

import me.gb2022.simpnet.client.ClientContext;
import me.gb2022.simpnet.client.ClientPacketHandler;
import me.gb2022.simpnet.packet.Packet;
import me.gb2022.simpnet.server.ServerContext;
import me.gb2022.simpnet.server.ServerPacketHandler;

public class EventPacketListener implements ClientPacketHandler<Packet>, ServerPacketHandler<Packet> {
    private final PacketEventBus eventBus;

    public EventPacketListener(PacketEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void handle(Packet packet, ClientContext context) {
        this.eventBus.call(packet);
    }

    @Override
    public void handle(Packet packet, ServerContext context) {
        this.eventBus.call(packet);
    }
}
