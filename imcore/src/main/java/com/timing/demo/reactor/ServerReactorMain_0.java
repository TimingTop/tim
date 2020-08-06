package com.timing.demo.reactor;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 服务端
 *
 * 主从 reactor 多线程
 *
 * 主 是 单线程，只 accept
 *
 * 从  是 多线程，
 *
 *
 *
 */

public class ServerReactorMain_0 {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int port = 6666;
    private ServerReactorSub_0 subReactor;// 从 reactor


    public ServerReactorMain_0(ServerReactorSub_0 subReactor) {
        try {
            this.subReactor = subReactor;
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.bind(new InetSocketAddress(port));
            // 一定要设置非阻塞，才能使用 selector
            listenChannel.configureBlocking(false);
            // 这个是 主 reactor ，只监听 accept 事件
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        System.out.println("*************************************************");
        System.out.println("主reactor： " + Thread.currentThread().getName());
        try {
            while(true) {
                int count = selector.select();
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        // 只处理 accept 的事件
                        if (key.isAcceptable()) {
                            SocketChannel accept = ((ServerSocketChannel) key.channel()).accept();
                            accept.configureBlocking(false);
                            System.out.println(accept.getRemoteAddress() + "==上线");

                            if (subReactor == null) {
                                Selector selector = key.selector();
                                // accept.register(selector, SelectionKey.OP_READ);
                            } else {
                                subReactor.register(accept);
                            }
                        }
                        // 把 当前项 remove 掉，否则会重复处理
                        iterator.remove();
                    }
                } else {
                    System.out.println("主reactor 等待连接");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
