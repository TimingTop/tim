package com.timing.im.network.connector;

import com.timing.im.network.endpoint.RawData;
import com.timing.im.protocol.core.Bytes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * 一个 endpoint 可以有 多个 connector
 *
 * connector 是跟 jdk socket 交互
 *
 *
 *
 *
 */
public class UdpConnector implements Connector {

    protected final InetSocketAddress localAddr;
    private boolean reuseAddress;

    protected volatile boolean running;
    private DatagramSocket socket;

    private int senderCount = 1;
    private int receiverCount = 1;
    private final List<Thread> receiverThreads = new LinkedList<>();
    private final List<Thread> senderThreads = new LinkedList<>();
    private final BlockingQueue<RawData> outgoing;

    public UdpConnector() {
        this.localAddr = new InetSocketAddress(0);
        this.running = false;
        // todo: 没有考虑 长度
        this.outgoing = new LinkedBlockingQueue<>();
    }


    @Override
    public synchronized void start() throws SocketException {

        // 把地址 放在 数据包 即可，socket 不需要
        DatagramSocket socket = new DatagramSocket(null);
        socket.setReuseAddress(true);
        socket.bind(localAddr);
    }

    protected void init(DatagramSocket socket) throws IOException {
        this.socket = socket;

        running = true;

        for (int i = 0; i < receiverCount; i++) {
            receiverThreads.add(new Receiver("aa"+ i));
        }
        for (int i = 0; i < senderCount; i++) {
            senderThreads.add(new Sender("sender"+i));
        }

        for (Thread t : receiverThreads) {
            t.start();
        }

        for (Thread t : senderThreads) {
            t.start();
        }

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    public void send(RawData msg) {
        boolean running;
        synchronized (this) {
            running = this.running;
            if (running) {
                outgoing.add(msg); // 把需要发送的数据放置队列中
            }
        }
        if (!running) {

        }
    }



    private class Receiver extends Thread {
        private DatagramPacket datagram;
        private int size;

        private Receiver(String name) {
            this.size = 2048 + 1;
            this.datagram = new DatagramPacket(new byte[size], size);
        }

        @Override
        public void run() {
            // 轮询 socket ，效率是非常非常差的
            // 后面要考虑怎么改成 channel + selector，但是原生是有 bug 的，可以上 netty
            while (running) {
                try {
                    work();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void work() throws IOException {
            datagram.setLength(size);
            DatagramSocket currentSocket = socket;
            if (currentSocket != null) {
                currentSocket.receive(datagram);
            }
            // 需要考虑 拿到的包过大

            byte[] bytes = Arrays.copyOfRange(datagram.getData(), datagram.getOffset(), datagram.getLength());
            RawData msg = RawData.inbound(bytes, System.nanoTime());
            // 处理 此 msg
        }
    }

    private class Sender extends Thread {
        private DatagramPacket datagram;
        public Sender(String name) {
            this.datagram = new DatagramPacket(Bytes.EMPTY, 0);
        }
        @Override
        public void run() {

        }

        public void work() throws Exception {
            // 循环获取阻塞队列的数据包发送
            RawData raw = outgoing.take();

            datagram.setData(raw.getBytes());
            // 从 raw 中获取到 需要发送到的地址
            datagram.setSocketAddress(null);

            DatagramSocket currentSocket = socket;
            if ( currentSocket != null) {

                // 这个是阻塞发送的， 所以多建 线程对提高性能没啥作用

                currentSocket.send(datagram);

            }
        }
    }

}
