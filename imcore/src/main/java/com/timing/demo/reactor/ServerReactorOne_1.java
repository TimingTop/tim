package com.timing.demo.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerReactorOne_1 implements Runnable {

    int cpuNums = Runtime.getRuntime().availableProcessors();
    int selectorNums;

    Selector[] selectorArr;

    // 处理 读写 事件的 线程池
    ExecutorService executorService;
    ServerSocketChannel serverSocketChannel;
    // 当前 selector
    Integer currentSelector;

    public ServerReactorOne_1() {
        currentSelector = 1;
        try {
            this.selectorNums = 3;
            selectorArr = new Selector[selectorNums];
            executorService = Executors.newFixedThreadPool(cpuNums + selectorNums);

            for (int i = 0; i < selectorNums; i++) {
                selectorArr[i] = SelectorProvider.provider().openSelector();
            }

            // 注册 accept 事件，只注册到 同一个
            SelectionKey register = serverSocketChannel.register(selectorArr[0], SelectionKey.OP_ACCEPT);
            register.attach(new Acceptor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey key) {
        if (key == null || key.attachment() == null) {
            return;
        }

        executorService.execute((Runnable) key.attachment());
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            Selector selector = selectorArr[i];
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // 如果 线程没有被中断 就一直跑
                    while(!Thread.currentThread().isInterrupted()) {
                        try {
                            selector.select(1000);
                            Set<SelectionKey> keys = selector.selectedKeys();
                            Iterator<SelectionKey> iterator = keys.iterator();
                            while (iterator.hasNext()) {
                                SelectionKey key = iterator.next();
                                iterator.remove();
                                dispatch(key);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    class Acceptor implements Runnable {
        Selector handlerSelector;
        Acceptor() {
            synchronized (currentSelector) {
                // selector 在 1,2 中选择，因为 0 是专门处理 accept 事件
                if (currentSelector == 3) {
                    currentSelector = 1;
                }
                this.handlerSelector = selectorArr[currentSelector];
                currentSelector++;
            }
        }

        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel == null) {
                    return;
                }
                socketChannel.configureBlocking(false);
                // 先注册一个 写的事件
                SelectionKey key = socketChannel.register(handlerSelector, SelectionKey.OP_READ);
                // 把当前的事件换掉，后面selector 就会拿到
                key.attach(new Handler(socketChannel, key));
                // 防止 register 与 select 竞争锁，
                this.handlerSelector.wakeup();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     *
     * 一个链接不应该有多个线程同时处理，但 reactor 模式下不同的事件 都会被 select 选到，可能造成多个 线程处理同一个 链接
     * 不能依靠 key 的 readable 或者 writeable 的状态 决定当前是 读或者写， 读写应该由完成的请求进行分割
     *
     *
     *
     *
     *
     */
    class Handler implements Runnable {
        private static final int READING = 0, WRITING = 1;
        volatile SocketChannel socketChannel;
        volatile SelectionKey key;

        int state = READING;

        byte[] readResult = null;
        ByteBuffer writeBuffer = null;

        /**
         *
         * 我们必须保证接受和回复的顺序性，保证客户端可以对响应做出正确的处理
         *
         * 可以将响应数据装入一个有序队列，顺序处理这些响应
         *
         * 或者通过令牌将请求和响应进行对应
         *
         *
         *
         */
        Handler(SocketChannel channel, SelectionKey key) {

            synchronized (this) {
                this.socketChannel = channel;
                this.key = key;
            }

        }

        @Override
        public synchronized void run() {
            try {
                if (state == READING) {
                    read();
                } else {
                    write();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public synchronized void read() throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            socketChannel.read(buffer);

            byte[] raw = buffer.array();

            if (raw != null && raw.length > 0) {
                readResult = raw;
                state = WRITING;
                key.interestOps(SelectionKey.OP_WRITE);
            }
        }

        public synchronized void write() throws IOException {
            if (this.readResult == null || this.readResult.length <= 0) {
                return;
            }

            if (writeBuffer == null) {

            }

            socketChannel.write(writeBuffer);
            if (writeBuffer.position() != writeBuffer.limit()) {
                // 没写完的, 就直接 返回，等一下 selector 被选到
                return;
            }

            key.interestOps(SelectionKey.OP_READ);
        }
    }
}
