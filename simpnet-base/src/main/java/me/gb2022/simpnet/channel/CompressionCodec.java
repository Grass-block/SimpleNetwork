package me.gb2022.simpnet.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class CompressionCodec extends ChannelDuplexHandler {
    private final Inflater inflater = new Inflater();
    private final Deflater deflater;
    private final int threshold;

    public CompressionCodec(int threshold, int level) {
        this.threshold = threshold;
        this.deflater = new Deflater(level);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ByteBuf data)) {
            return;
        }

        data.resetReaderIndex();

        var sig = data.readByte();

        if (sig == 0x0) {
            data.markReaderIndex();
            ctx.fireChannelRead(data);
            return;
        } else if (sig != 0xF) {
            throw new IllegalArgumentException("invalid compression protocol sig!");
        }

        var len = data.readInt();
        var copied = data.copy();
        data.release();

        data = ctx.alloc().ioBuffer(len + 512);

        this.inflater.reset();
        this.inflater.setInput(copied.nioBuffer());

        data.clear();
        data.writerIndex(0);

        while (!this.inflater.finished()) {
            if (!data.isWritable()) {
                data.ensureWritable(512);
            }

            var produced = this.inflater.inflate(data.nioBuffer(data.writerIndex(), data.writableBytes()));
            data.writerIndex(data.writerIndex() + produced);
        }

        copied.release();
        ctx.fireChannelRead(data);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf data)) {
            return;
        }

        if (data.writerIndex() < this.threshold) {
            var content = data.copy();

            data.clear();
            data.writerIndex(0);
            data.readerIndex(0);

            data.writeByte(0x0);
            data.writeBytes(content);

            ctx.write(data, promise);
            content.release();
            return;
        }

        this.deflater.reset();
        var a = data.copy();
        this.deflater.setInput(a.nioBuffer());

        var len = data.writerIndex();

        data.clear();
        data.writerIndex(0);
        data.writeByte(0xF);
        data.writeInt(len);


        this.deflater.finish();

        while (!this.deflater.finished()) {
            if (!data.isWritable()) {
                data.ensureWritable(512);
            }

            var produced = this.deflater.deflate(data.nioBuffer(data.writerIndex(), data.writableBytes()));
            data.writerIndex(data.writerIndex() + produced);
        }

        ctx.write(data, promise);
        a.release();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        deflater.end();
        inflater.end();
        super.handlerRemoved(ctx);
    }
}
