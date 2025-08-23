package me.gb2022.simpnet.jkcp.server;

import io.netty.buffer.ByteBuf;
import me.gb2022.simpnet.jkcp.JKCPNetworkServer;
import me.gb2022.simpnet.server.NetworkServer;
import me.gb2022.simpnet.server.ServerContext;
import org.beykery.jkcp.KcpOnUdp;

import java.net.InetSocketAddress;

public final class KCPServerContext implements ServerContext {
    private final JKCPNetworkServer server;
    private final InetSocketAddress clientAddress;
    private final KcpOnUdp peer;

    public KCPServerContext(JKCPNetworkServer server, KcpOnUdp kcp) {
        this.server = server;
        this.clientAddress = kcp.getRemote();
        this.peer = kcp;
    }

    @Override
    public void send(ByteBuf message) {
        this.peer.send(message);
    }

    @Override
    public void disconnect() {
        this.peer.close();
    }

    @Override
    public NetworkServer getServer() {
        return this.server;
    }

    @Override
    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }

    public KcpOnUdp getPeer() {
        return this.peer;
    }
}
