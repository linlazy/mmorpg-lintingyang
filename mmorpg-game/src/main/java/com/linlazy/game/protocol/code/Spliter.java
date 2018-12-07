package com.linlazy.game.protocol.code;

import com.linlazy.game.protocol.packet.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


/**
 * 长度解码，魔数不对则数据包过滤
 */
public class Spliter extends LengthFieldBasedFrameDecoder {

    private static final int LENGTH_FIELD_OFFSET =7;
    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter(int maxFrameLength) {
        super(maxFrameLength, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if(in.getInt(in.readerIndex()) != PacketCodeC.MAGIC_NUMBER ){
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
