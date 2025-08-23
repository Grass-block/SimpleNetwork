package me.gb2022.simpnet.jkcp.client;

import io.netty.buffer.ByteBuf;
import me.gb2022.simpnet.client.ClientContext;
import me.gb2022.simpnet.client.ClientListener;
import me.gb2022.simpnet.client.ClientListenerAdapter;
import org.beykery.jkcp.KcpListerner;
import org.beykery.jkcp.KcpOnUdp;

public final class KCPClientListenerAdapter extends ClientListenerAdapter implements KcpListerner {
    public KCPClientListenerAdapter(ClientListener listener, ClientContext context) {
        super(listener, context);
    }

    void onConnect(ClientContext context) {
        this.listener.onConnect(context);
    }

    @Override
    public void handleReceive(ByteBuf bb, KcpOnUdp kcp) {
        this.listener.handleMessage(bb, this.context);
    }

    @Override
    public void handleException(Throwable ex, KcpOnUdp kcp) {
        this.listener.handleException(ex, this.context);
    }

    @Override
    public void handleClose(KcpOnUdp kcp) {
        this.listener.onDisconnect(this.context);
    }
}
