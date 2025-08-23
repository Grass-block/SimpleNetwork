package me.gb2022.simpnet.packet;

import io.netty.buffer.ByteBuf;

public interface PacketCodec<I extends Packet> {
    void encode(I packet, ByteBuf buffer) throws Throwable;

    I decode(ByteBuf buffer) throws Throwable;
}
