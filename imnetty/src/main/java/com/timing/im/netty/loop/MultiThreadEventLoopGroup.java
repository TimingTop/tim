package com.timing.im.netty.loop;

import com.timing.im.netty.channel.Channel;
import com.timing.im.netty.channel.ChannelFuture;
import com.timing.im.netty.util.executor.DefaultEventExecutorChooserFactory;
import com.timing.im.netty.util.executor.EventExecutor;
import com.timing.im.netty.util.executor.EventExecutorChooserFactory;

import java.util.concurrent.Executor;

public class MultiThreadEventLoopGroup implements EventLoopGroup{

    private final EventExecutor[] children;
    private final EventExecutorChooserFactory.EventExecutorChooser chooser;


    public MultiThreadEventLoopGroup(int nThreads, Executor executor) {
        children = new EventExecutor[nThreads]; // children = eventLoop ,里面包了 selector
        // 初始化

        chooser = DefaultEventExecutorChooserFactory.INSTANCE.newChooser(children);
    }

    @Override
    public ChannelFuture register(Channel channel) {
        return null;
    }

    public EventExecutor next() {
        return chooser.next();
    }
}
