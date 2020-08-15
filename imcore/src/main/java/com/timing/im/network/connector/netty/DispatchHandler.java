package com.timing.im.network.connector.netty;

import com.timing.im.network.channel.RawDataChannel;
import com.timing.im.network.endpoint.RawData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DispatchHandler extends ChannelInboundHandlerAdapter {
    private final RawDataChannel rawDataChannel;

    public DispatchHandler(RawDataChannel rawDataChannel) {
        this.rawDataChannel = rawDataChannel;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        rawDataChannel.receiveData((RawData)msg);
    }
}
