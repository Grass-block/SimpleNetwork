package me.gb2022.simpnet.jkcp.client;

import io.netty.buffer.ByteBuf;
import me.gb2022.simpnet.client.ClientContext;
import me.gb2022.simpnet.client.NetworkClient;
import me.gb2022.simpnet.jkcp.JKCPNetworkClient;

public final class KCPClientContext implements ClientContext {
    private final JKCPNetworkClient client;

    public KCPClientContext(JKCPNetworkClient client) {
        this.client = client;
    }

    @Override
    public void send(ByteBuf message) {
        this.client.send(message);
    }

    @Override
    public void disconnect() {
        this.client.disconnect();
    }

    @Override
    public NetworkClient getClient() {
        return this.client;
    }
}
