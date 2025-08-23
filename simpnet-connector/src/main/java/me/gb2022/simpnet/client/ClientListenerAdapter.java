package me.gb2022.simpnet.client;

public abstract class ClientListenerAdapter {
    protected final ClientListener listener;
    protected final ClientContext context;

    public ClientListenerAdapter(ClientListener listener, ClientContext context) {
        this.listener = listener;
        this.context = context;
    }
}
