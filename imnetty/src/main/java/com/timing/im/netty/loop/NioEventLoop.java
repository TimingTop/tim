package com.timing.im.netty.loop;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Queue;

public class NioEventLoop implements Runnable {

    private Selector selector;
    private SelectorProvider provider;

    private Queue<Runnable> tailTasks;


    public NioEventLoop(NioEventLoopGroup nioEventLoopGroup, SelectorProvider selectorProvider) {

        provider = selectorProvider;

        try {
            selector = selectorProvider.openSelector();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Queue<Runnable> newTaskQueue() {
        return null;
    }

    @Override
    public void run() {
        for (;;) {
            int strategy = 0;
            try {
                if (!tailTasks.isEmpty()) {
                    strategy = selector.selectNow();
                } else {
                    strategy = -1;
                }

                switch (strategy) {
                    case -2:
                        continue;
                    case -3:
                    case -1:
                        if (tailTasks.isEmpty()) {
                            strategy = selector.select(20);
                        }
                    default:
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }
}
