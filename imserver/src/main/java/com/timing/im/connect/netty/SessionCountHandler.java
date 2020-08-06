package com.timing.im.connect.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 *
 * 此方法是验证使用的，验证 每个socket 是否 建一个唯一的 handler 保存 socket 的上下文
 *
 * 类似 mina 的 ioSession 作用
 *
 *
 *
 */
public class SessionCountHandler extends ChannelInboundHandlerAdapter {

    private AttributeKey<Integer> attributeKey = AttributeKey.valueOf("counter");


    private int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        counter++;

        System.out.println("第几次访问: " + counter);


//        super.channelRead(ctx, msg);
    }
}
