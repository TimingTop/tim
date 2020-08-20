package com.timing.im.netty.channel;

public interface ChannelFactory<T extends Channel> {

    T newChannel();
}
