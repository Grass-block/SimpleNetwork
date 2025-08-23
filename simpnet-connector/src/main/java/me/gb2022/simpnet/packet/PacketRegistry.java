package me.gb2022.simpnet.packet;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PacketRegistry {
    private final Map<Class<?>, PacketCodec<?>> packet2codec = new HashMap<>();
    private final Map<String, PacketCodec<?>> id2codec = new HashMap<>();

    public <T extends Packet> void addPacket(Class<T> packet, PacketCodec<T> codec) {
        this.packet2codec.put(packet, codec);
        this.id2codec.put(Packet.getId(packet), codec);
    }

    public <T extends Packet> PacketCodec getCodec(Class<T> packetClazz) {
        return this.packet2codec.get(packetClazz);
    }

    public PacketCodec getCodec(String id) {
        return this.id2codec.get(id);
    }

    public Packet decode(String id, ByteBuf message) {
        try {
            return this.getCodec(id).decode(message);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void encode(Packet packet, ByteBuf message) {
        try {
            this.getCodec(packet.getClass()).encode(packet, message);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
