package me.gb2022.simpnet.client;

import io.netty.buffer.ByteBuf;
import me.gb2022.simpnet.PacketHolder;
import me.gb2022.simpnet.packet.Packet;
import me.gb2022.simpnet.packet.PacketRegistry;

import java.net.InetSocketAddress;

public abstract class NetworkClient extends PacketHolder {
    private final ClientPacketMessageHandler handler = new ClientPacketMessageHandler();

    public abstract void connect(InetSocketAddress address);

    public abstract void disconnect();

    public abstract void send(ByteBuf message);

    public abstract void addListener(ClientListener listener);

    public abstract void removeListener(ClientListener listener);

    public abstract InetSocketAddress getServerAddress();

    public final ClientPacketMessageHandler getPacketHandler() {
        return this.handler;
    }

    public void sendPacket(Packet packet) {
        this.send(Packet.encode(this.getRegistry(), packet));
    }

    public <T extends Packet> void addHandler(Class<T> typeOfT, ClientPacketHandler<T> handler) {
        this.handler.addHandler(handler, typeOfT);
    }

    public <T extends Packet> void removeHandler(ClientPacketHandler<T> handler) {
        this.handler.removeHandler(handler);
    }

    public void injectPacketProcessor() {
        this.addListener(this.handler);
    }
}
