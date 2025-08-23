package me.gb2022.simpnet;

import me.gb2022.simpnet.packet.PacketRegistry;
import me.gb2022.simpnet.packet.event.EventPacketListener;
import me.gb2022.simpnet.packet.event.PacketEventBus;

public abstract class PacketHolder {
    private final PacketEventBus eventBus = new PacketEventBus();
    private final EventPacketListener listener = new EventPacketListener(this.eventBus);
    private final PacketRegistry registry = new PacketRegistry();

    public PacketRegistry getRegistry() {
        return registry;
    }

    public EventPacketListener getEventBusHandler() {
        return listener;
    }

    public PacketEventBus getEventBus() {
        return eventBus;
    }
}
