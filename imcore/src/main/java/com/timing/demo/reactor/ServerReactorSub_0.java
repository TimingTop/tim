package com.timing.demo.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端
 *
 * 主从 多线程 reactor 实现
 *
 * 主 是 单线程
 *
 * 从 是 多线程
 *
 *
 *
 *
 *
 */
public class ServerReactorSub_0 {
    private Selector selector;
    private volatile boolean stop;
    // 启动 5 个线程 处理 读写事件
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public ServerReactorSub_0() {
        try {
            selector = SelectorProvider.provider().openSelector();
            stop = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 主 reactor 只监听 accept 事件，所以 accept 之外的事件需要注册到 从reactor
    // 所以 从reactor 要放置在 主reactor 里面，方便操作
    public void register(SocketChannel sc) {
        try {
            sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("*******************************************");
                System.out.println("Sub reactor start, name = " + Thread.currentThread().getName());

                while (!stop) {
                    try {
                        selector.select(1000);
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        SelectionKey key = null;
                        while (iterator.hasNext()) {
                            key = iterator.next();
                            // 要删掉，否则会 重复 处理
                            iterator.remove();

                            try {
                                // 分发业务
                                dispatch(key);
                            } catch (Exception e) {
                                if (key != null) {
                                    key.cancel();
                                    if (key.channel() != null) {
                                        key.channel().close();
                                    }
                                }
                            }


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void dispatch(SelectionKey key) {
        // 从reactor 只做读写操作
        if (key.isValid()) {
            if (key.isReadable()) {
                readData(key);
            }
        }
    }
    // 处理 读请求
    private void readData(SelectionKey key) {
        SocketChannel channel = null;

        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            String message = new String(buffer.array());

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // 源数据 处理程序，需要用 线程
                    if (count > 0) {
                        // 输出该 消息，
                        System.out.println("from client： " + message);
                    }
                }
            });
        } catch (IOException e) {
            try {
                key.cancel();
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }


    }
}
