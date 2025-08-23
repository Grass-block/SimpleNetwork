package me.gb2022.simpnet.packet.event;

public interface Cancellable {
    void setCancel(boolean cancel);

    void cancel();

    boolean isCancelled();
}
