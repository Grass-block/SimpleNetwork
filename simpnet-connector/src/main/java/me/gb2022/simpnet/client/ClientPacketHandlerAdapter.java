package me.gb2022.simpnet.client;

import me.gb2022.simpnet.packet.Packet;

public final class ClientPacketHandlerAdapter<T extends Packet> {
    private final ClientPacketHandler<T> handler;
    private final Class<T> clazz;

    public ClientPacketHandlerAdapter(ClientPacketHandler<T> handler, Class<T> clazz) {
        this.handler = handler;
        this.clazz = clazz;
    }

    public void handle(Packet packet, ClientContext context) {
        if (!this.clazz.isInstance(packet)) {
            return;
        }
        this.handler.handle(this.clazz.cast(packet), context);
    }
}
