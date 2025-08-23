package me.gb2022.simpnet.server;

import io.netty.buffer.ByteBuf;
import me.gb2022.simpnet.PacketHolder;
import me.gb2022.simpnet.packet.Packet;

import java.net.InetSocketAddress;

public abstract class NetworkServer extends PacketHolder {
    private final ServerPacketMessageHandler handler = new ServerPacketMessageHandler();

    public abstract void start(InetSocketAddress address);

    public abstract void stop();

    public abstract void send(InetSocketAddress address, ByteBuf message);

    public abstract void broadcast(ByteBuf message);

    public abstract void addListener(ServerListener listener);

    public abstract void removeListener(ServerListener listener);

    public abstract InetSocketAddress getServerAddress();

    public final ServerPacketMessageHandler getPacketHandler() {
        return this.handler;
    }

    public void sendPacket(InetSocketAddress address, Packet packet) {
        this.send(address, Packet.encode(this.getRegistry(), packet));
    }

    public <T extends Packet> void addHandler(Class<T> typeOfT, ServerPacketHandler<T> handler) {
        this.handler.addHandler(handler, typeOfT);
    }

    public <T extends Packet> void removeHandler(ServerPacketHandler<T> handler) {
        this.handler.removeHandler(handler);
    }

    public void injectPacketProcessor() {
        this.addListener(this.handler);
        this.handler.addGenericHandler(this.getEventBusHandler());
    }

    public abstract void disconnect(InetSocketAddress address);
}
