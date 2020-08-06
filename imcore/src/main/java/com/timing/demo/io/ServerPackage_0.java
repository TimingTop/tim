package com.timing.demo.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class ServerPackage_0 extends Thread {
    ServerSocketChannel serverSocketChannel = null;
    Selector selector = null;
    SelectionKey selectionKey = null;

    public void init() throws Exception {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(7000));

        selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        while (true) {
            try {
                int selectKey = selector.select();
                if ( selectKey > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {

                        }
                        if (key.isReadable()) {

                        }
                        if (key.isWritable()) {

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void accept(SelectionKey key) {
        try {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverChannel.accept();

            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(SelectionKey key) {

        try {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            int len = channel.read(byteBuffer);
            if (len > 0) {
                byteBuffer.flip();
                byte[] byteArray = new byte[byteBuffer.limit()];
                byteBuffer.get(byteArray);
                System.out.println("Server receive -- " + new String(byteArray));
            }

            selectionKey.interestOps(SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        String httpResponse = "HTTP/1.1 200 OK\r\n" + "Content-Length: 38\r\n" + "Content-Type: text/html\r\n" + "\r\n"
                + "<html><body>Hello World!</body></html>";
        System.out.println("response from server to client");
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(httpResponse.getBytes());
            while (byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }
            selectionKey.cancel();
        } catch (IOException e) {
            try {
                selectionKey.cancel();
                serverSocketChannel.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
