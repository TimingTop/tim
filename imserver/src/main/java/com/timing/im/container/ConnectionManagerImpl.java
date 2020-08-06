package com.timing.im.container;

import com.timing.im.IMServer;
import com.timing.im.connect.listener.ConnectionListener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectionManagerImpl implements Module {

    InetAddress bindAddress = null;
    InetAddress adminConsoleBindAddress = null;

    private final ConnectionListener serverListener;


    public ConnectionManagerImpl() throws UnknownHostException {

        InetAddress bindAddress = Inet4Address.getByName("127.0.0.1");
        // 使用 netty
        serverListener = new ConnectionListener(bindAddress, 9988);
        try {
            bindAddress = Inet4Address.getLocalHost();



        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return null;
    }

    public void initialize(IMServer server) {

    }

    public void start() {

        // 把所有的 connection listener 启动起来

        startListeners();
    }

    public void startListeners() {
        serverListener.start();
    }

    public void stop() {

    }

    public void destroy() {

    }
}
