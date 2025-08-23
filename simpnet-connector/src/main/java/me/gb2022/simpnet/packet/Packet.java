package me.gb2022.simpnet.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.StandardCharsets;

public interface Packet {
    int HEADER_LENGTH_LIMIT = 64;
    int PACKET_SIZE_LIMIT = 1024;
    byte PACKET_HEADER = 0xF;

    static void writePacketId(Packet packet, ByteBuf target) {
        byte[] head = packet.getPacketId().getBytes(StandardCharsets.US_ASCII);

        if (head.length > HEADER_LENGTH_LIMIT) {
            throw new RuntimeException("header too long!");
        }

        target.writeByte(head.length);
        target.writeBytes(head);
    }

    static String readPacketId(ByteBuf target) {
        byte headerLength = target.readByte();

        if (headerLength > HEADER_LENGTH_LIMIT) {
            throw new RuntimeException("header too long!");
        }

        byte[] head = new byte[headerLength];

        target.readBytes(head);

        return new String(head, StandardCharsets.US_ASCII);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    static boolean isPacket(ByteBuf message) {
        int index = message.readerIndex();
        boolean result = message.readByte() == Packet.PACKET_HEADER;
        message.readerIndex(index);
        return result;
    }

    static ByteBuf encode(PacketRegistry registry, Packet packet) {
        try {
            ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();
            buffer.resetWriterIndex();
            buffer.writeByte(PACKET_HEADER);

            ByteBuf data = ByteBufAllocator.DEFAULT.ioBuffer();
            registry.encode(packet,data);
            int len = data.writerIndex();

            if (len > PACKET_SIZE_LIMIT) {
                throw new RuntimeException("data too long!");
            }

            writePacketId(packet, buffer);

            buffer.writeShort(len);
            buffer.writeBytes(data);

            data.release();
            return buffer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static Packet decode(PacketRegistry registry, ByteBuf buffer) {
        try {
            buffer.resetReaderIndex();
            buffer.readByte();
            String id = readPacketId(buffer);
            short len = buffer.readShort();

            if (len < 0) {
                throw new RuntimeException("invalid data size!");
            }

            ByteBuf data = ByteBufAllocator.DEFAULT.ioBuffer(len);
            buffer.readBytes(data, len);
            Packet packet = registry.decode(id, data);
            data.release();
            return packet;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String getId(Class<?> packet) {
        return packet.getAnnotation(SimpnetPacket.class).value();
    }

    default String getPacketId() {
        return getId(getClass());
    }
}
