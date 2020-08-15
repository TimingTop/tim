package com.timing.im.network.connector;

import com.timing.im.network.channel.RawDataChannel;
import com.timing.im.network.endpoint.RawData;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UdpNIOConnector implements Connector {

    protected final InetSocketAddress localAddr;

    protected volatile boolean running;
    private DatagramChannel channel;
    private Selector selector;

    private final BlockingQueue<RawData> outgoing;

    public UdpNIOConnector() {
        this.localAddr = new InetSocketAddress(0);
        this.running = false;

        this.outgoing = new LinkedBlockingQueue<>();
    }

    @Override
    public synchronized void start() throws IOException {
        this.channel = DatagramChannel.open(); //打开一个 channel
        this.channel.configureBlocking(false);
        this.channel.bind(localAddr);

        this.selector = Selector.open();


    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void setRawDataReceiver(RawDataChannel messageHandler) {

    }

    private class Sender extends Thread {
        private ByteBuffer buffer = ByteBuffer.allocate(2048);

        public Sender(String name) {

        }
        @Override
        public void run() {
            while (running) {
                try {
                    work();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void work() throws Exception {
            RawData raw = outgoing.take();
            buffer.put(raw.getBytes());
            buffer.flip(); // 写 转为 读

            channel.send(buffer, localAddr);
            buffer.clear();
        }
    }
}
