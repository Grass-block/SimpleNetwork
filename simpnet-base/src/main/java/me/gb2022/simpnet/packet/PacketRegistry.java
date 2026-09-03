package me.gb2022.simpnet.packet;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class PacketRegistry {
    public static final PacketRegistry REGISTRY = new PacketRegistry();
    private final List<Class<? extends Packet>> i2c = new ArrayList<>();
    private final Map<Class<? extends Packet>, Integer> c2i = new HashMap<>();
    private boolean debug = false;

    public PacketRegistry() {
        super();
    }

    public PacketRegistry(int cap, Consumer<PacketRegistry> initializer) {
        this.allocate(cap);
        initializer.accept(this);
    }

    public PacketRegistry(int cap, boolean debug, Consumer<PacketRegistry> initializer) {
        this.setDebug(debug);
        this.allocate(cap);
        initializer.accept(this);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void allocate(int cap) {
        for (var i = 0; i < cap; i++) {
            this.i2c.add(null);
        }
    }

    public void register(int id, Class<? extends Packet> packet) {
        this.i2c.set(id, packet);
        this.c2i.put(packet, id);

        if (this.debug) {
            System.out.println("Registered packet: " + id + " -> " + packet.getName() + " ,c2i= " + this.c2i.get(packet));
        }
    }

    public void encode(Packet packet, ByteBuf data) {
        try {
            data.resetReaderIndex();
            data.resetWriterIndex();

            var id = this.c2i.get(packet.getClass());

            if (id == null) {
                if(this.debug){
                    System.out.println(this.c2i);
                }
                throw new IllegalArgumentException("Invalid packet: " + packet.getClass() + "@" + packet.getClass().hashCode());
            }

            data.writeByte(id);
            packet.write(data);

            if (this.debug) {
                System.out.println("[encode]" + packet.getClass().getSimpleName() + "->" + id);
            }
        } catch (Exception e) {
            throw new InvalidPacketFormatException(e);
        }
    }

    public Packet decode(ByteBuf data) {
        try {
            var id = data.readByte();
            var clazz = this.i2c.get(id);

            if (this.debug) {
                System.out.println("[decode]" + id + "->" + clazz);
            }
            if (clazz == null) {
                throw new RuntimeException("Unknown packet id: " + id);
            }

            return clazz.getDeclaredConstructor(ByteBuf.class).newInstance(data);
        } catch (Exception e) {
            throw new InvalidPacketFormatException(e);
        }
    }
}
