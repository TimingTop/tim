package com.timing.im.netty.loop;

import com.timing.im.netty.channel.Channel;
import com.timing.im.netty.channel.ChannelFuture;

public interface EventLoopGroup {

    ChannelFuture register(Channel channel);
}
