package com.timing.im.network.connector.netty;

import com.timing.im.network.endpoint.RawData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.math.BigInteger;
import java.util.List;

// 这里 基于  tcp 的 CoAP 的协议进行解析
//  协议的 官方文档
// https://tools.ietf.org/id/draft-ietf-core-coap-tcp-tls-11.html#:~:text=Conceptually%2C%20CoAP%20over%20TCP%20replaces,the%20UDP%2FDTLS%20datagram%20layer.



public class DatagramFramer extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() > 0) {
            // 这里要进行 协议解析，如果 长度 不够的话，当前直接退出，不读任何 字段
            // 把第一个字节，高4位是 option + payload 的长度， 后四位是 token 的长度
            byte firstByte = in.getByte(in.readerIndex());
            int lengthNibble = (firstByte & 0xF0) >>> 4; // 高4位
            int tokenNibble = (firstByte & 0x0F);    // 低4 位

            int lengthFieldSize = getLengthFieldSize(lengthNibble);// 扩展长度 byte 数
            int coapHeaderSize = getCoapHeaderSize(lengthFieldSize, tokenNibble);
            if (in.readableBytes() < coapHeaderSize) {
                // 如果可读的 size 少于 协议头的 size，就等待
                return;
            }

            // 获取 body 的长度
            int bodyLength = getBodyLength(in, lengthNibble, lengthFieldSize);

            if (in.readableBytes() < coapHeaderSize + bodyLength) {
                // 数据 不完整，就等待 下一次
                return;
            }

            // 把完整的 协议包读出来
            byte[] data = new byte[coapHeaderSize + bodyLength];
            in.readBytes(data);

            Channel channel = ctx.channel();

            RawData rawData = RawData.inbound(data, System.nanoTime());
            out.add(rawData);

        }
    }

    // 判断需要在 Extended Length 拿多少字节 当做长度
    private int getLengthFieldSize(int len) {
        if (len > 15 || len < 0) {
            throw new IllegalArgumentException("Invalid len field: " + len);
        }

        if (len  == 13) {
            return 1;
        } else if (len == 14) {
            return 2;
        } else if (len == 15) {
            return 4;
        } else {
            return 0;
        }
    }

    // 这个是 协议头的 的长度，
    private int getCoapHeaderSize(int lengthFieldSize, int tokenFieldSize) {
        return 1          // Len + TKL  固定 1 byte
                + lengthFieldSize  // 根据第一个字节解析的 byte
                + 1    // Code
                + tokenFieldSize;  // token 的长度

        // 协议 Options
    }

    // 获取 协议 Options 开始的数据段的长度
    private int getBodyLength(ByteBuf in, int lengthNibble, int fieldSize) {
        byte data[] = new byte[fieldSize];
        in.getBytes(in.readerIndex() + 1, data);

        switch (fieldSize) {
            case 0:
                return lengthNibble;
            case 1:
                return new BigInteger(1, data).intValue() + 13;
            case 2:
                return new BigInteger(1, data).intValue() + 269;
            case 4:
                // todo: size > 2GB ，可以 overflow 了， 自己看着办
                return new BigInteger(1, data).intValue() + 65805;
            default:
                // 协议写的，扩展长度字节最多 4 byte
                throw new IllegalArgumentException("Invalid field size: " + fieldSize);
        }
    }



}
