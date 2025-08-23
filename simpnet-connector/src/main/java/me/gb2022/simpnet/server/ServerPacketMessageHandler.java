package me.gb2022.simpnet.server;

import io.netty.buffer.ByteBuf;
import me.gb2022.simpnet.packet.Packet;
import me.gb2022.simpnet.packet.event.EventPacketListener;

import java.util.HashMap;

public final class ServerPacketMessageHandler implements ServerListener {
    private final HashMap<ServerPacketHandler<?>, ServerPacketHandlerAdapter<?>> handlers = new HashMap<>(32);


    @Override
    public void handleMessage(ByteBuf message, ServerContext context) {
        if (!Packet.isPacket(message)) {
            return;
        }
        Packet packet = Packet.decode(context.getServer().getRegistry(), message);
        for (ServerPacketHandlerAdapter<?> handler : this.handlers.values()) {
            handler.handle(packet, context);
        }
    }

    public <T extends Packet> void addHandler(ServerPacketHandler<T> handler, Class<T> typeOfT) {
        if (this.handlers.containsKey(handler)) {
            return;
        }
        ServerPacketHandlerAdapter<T> handlerAdapter = new ServerPacketHandlerAdapter<>(handler, typeOfT);
        this.handlers.put(handler, handlerAdapter);
    }

    public <T extends Packet> void removeHandler(ServerPacketHandler<T> handler) {
        if (!this.handlers.containsKey(handler)) {
            return;
        }
        this.handlers.remove(handler);
    }

    public void addGenericHandler(EventPacketListener eventBusHandler) {
        //
    }
}
