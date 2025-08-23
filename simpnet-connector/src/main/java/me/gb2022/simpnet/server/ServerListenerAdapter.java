package me.gb2022.simpnet.server;

public abstract class ServerListenerAdapter {
    protected final ServerListener listener;

    public ServerListenerAdapter(ServerListener listener) {
        this.listener = listener;
    }
}
