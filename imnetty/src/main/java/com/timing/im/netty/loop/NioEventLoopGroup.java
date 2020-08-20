package com.timing.im.netty.loop;

import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.Executor;

public class NioEventLoopGroup extends MultiThreadEventLoopGroup {

    private NioEventLoop[] children;



    public NioEventLoopGroup(int nThreads, Executor executor, SelectorProvider selectorProvider) {

        super(nThreads, executor);
        // 获取 jdk 实现
        selectorProvider.provider();
        // 获取 系统有关 的实现 实例
//        selectorProvider.openSelector();
        if (executor == null) {
//            executor = new ThreadPerTaskExecutor();
        }

        children = new NioEventLoop[nThreads];

        // 初始化 NioEventLoop 数组

        for (int i = 0; i < children.length; i++) {
            children[i] = newChild(selectorProvider);
        }
    }

    public NioEventLoop newChild(SelectorProvider selectorProvider) {

        return new NioEventLoop(this, selectorProvider);
    }



}
