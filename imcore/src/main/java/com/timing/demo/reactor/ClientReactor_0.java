package com.timing.demo.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 *
 *
 * 这个是客户端
 *
 *
 *
 *
 */
public class ClientReactor_0 {

    private final String Host = "127.0.0.1"; // 服务器ip
    private final int port = 6666; // 服务器端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public ClientReactor_0() throws IOException {
        selector = SelectorProvider.provider().openSelector();
        socketChannel = SocketChannel.open(new InetSocketAddress(Host, port));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println( username + " is ok .....");


    }

    public void send(String message) {
        message = username + "say: " + message;

        try {
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void readInfo() {
        try {
            int select = selector.select();
            if (select > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while ( iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
                iterator.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
