package me.gb2022.simpnet.server;

import io.netty.buffer.ByteBuf;

public interface ServerListener {
    default void onDisconnect(ServerContext context) {
    }

    default void handleMessage(ByteBuf message, ServerContext context) {
    }

    default void handleException(Throwable exception, ServerContext context) {
    }
}
