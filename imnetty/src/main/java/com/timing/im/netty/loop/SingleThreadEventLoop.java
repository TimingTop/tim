package com.timing.im.netty.loop;

import com.timing.im.netty.channel.Channel;
import com.timing.im.netty.channel.ChannelFuture;

public class SingleThreadEventLoop implements EventLoopGroup {

    @Override
    public ChannelFuture register(Channel channel) {
        return null;
    }
}
