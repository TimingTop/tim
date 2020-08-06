package com.timing.demo.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

public class ClientPackage_0 extends Thread {

    private SocketChannel socketChannel;
    private Selector selector = null;
    private int clientId;



    public void init() throws IOException {
        InetSocketAddress address = new InetSocketAddress(7000);
        selector = SelectorProvider.provider().openSelector();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(address);
        synchronized (selector) {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                int key = selector.select();
                if (key > 0) {
                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keySet.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();

                        if (selectionKey.isConnectable()) {
                            connect(selectionKey);
                        }
                        if (selectionKey.isWritable()) {
                            send(selectionKey);
                        }
                        if (selectionKey.isReadable()) {
                            read(selectionKey);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void connect(SelectionKey key) {
        System.out.println("client -- connect");
        SocketChannel channel = (SocketChannel) key.channel();

        try {
            channel.finishConnect();
            channel.register(selector, SelectionKey.OP_WRITE);
            key.interestOps(SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int len = channel.read(byteBuffer);
        if (len > 0) {
            byteBuffer.flip(); // 写转 读
            byte[] byteArray = new byte[byteBuffer.limit()];
            byteBuffer.get(byteArray);

            System.out.println("client["+clientId+"] receive -- " + new String(byteArray));

            len = channel.read(byteBuffer);
            byteBuffer.clear();
        }
        key.interestOps(SelectionKey.OP_READ);
    }

    public void send(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();

        for (int i = 0; i < 10; i++) {
            String serverStr = i + "-Server, how are you? Today is a beautiful day.";
            ByteBuffer byteBuffer = ByteBuffer.wrap(serverStr.getBytes());
            while (byteBuffer.hasRemaining()) {
                try {
                    channel.write(byteBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            channel.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }
}
