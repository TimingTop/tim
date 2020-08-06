package com.timing.im.connect.listener;

import com.timing.im.connect.netty.NettyConnectionAcceptor;
import com.timing.im.container.BaseModule;
import com.timing.im.container.ConnectionManagerImpl;

import java.net.InetAddress;


public class ConnectionListener {

    private final InetAddress bindAddress;
    private final int defaultPort;

    public ConnectionListener(InetAddress bindAddress, int defaultPort) {
        this.bindAddress = bindAddress;
        this.defaultPort = defaultPort;
    }


    public synchronized void start() {

        System.out.println("start connect");
        NettyConnectionAcceptor acceptor = new NettyConnectionAcceptor(bindAddress, defaultPort);
        acceptor.start();
    }
}
