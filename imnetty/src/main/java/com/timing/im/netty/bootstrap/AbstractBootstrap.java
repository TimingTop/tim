package com.timing.im.netty.bootstrap;

import com.timing.im.netty.channel.Channel;
import com.timing.im.netty.channel.ChannelFactory;
import com.timing.im.netty.channel.ChannelFuture;
import com.timing.im.netty.channel.ReflectiveChannelFactory;

public class AbstractBootstrap<B extends AbstractBootstrap<B, C>, C extends Channel> {

    private volatile ChannelFactory<? extends C> channelFactory;

    AbstractBootstrap() {

    }

    // 根据 socketChannel or  serverSocketChannel 生成反射工厂
    // 下面的应用会用到
    public B channel(Class<? extends C> channelClass) {
        ReflectiveChannelFactory<C> factory = new ReflectiveChannelFactory<>(channelClass);
        return channelFactory(factory);
    }

    public B channelFactory(ChannelFactory<? extends C> channelFactory) {
        this.channelFactory = channelFactory;
        return self();
    }

    private B self() {
        return (B) this;
    }


    final ChannelFuture initAndRegister() {
        Channel channel = null;
        // 拿到 socketChannel
        channel = this.channelFactory.newChannel();

        // 把 channel 注册进入 ChannelFuture
        return null;
    }
}
