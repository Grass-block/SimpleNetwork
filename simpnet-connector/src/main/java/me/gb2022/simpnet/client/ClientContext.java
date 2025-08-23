package me.gb2022.simpnet.client;

import io.netty.buffer.ByteBuf;
import me.gb2022.simpnet.packet.Packet;

import java.net.InetSocketAddress;

public interface ClientContext {
    void send(ByteBuf message);

    void disconnect();

    NetworkClient getClient();

    default void sendPacket(Packet packet){
        this.getClient().sendPacket(packet);
    }

    default InetSocketAddress getServerAddress(){
        return this.getClient().getServerAddress();
    }
}
