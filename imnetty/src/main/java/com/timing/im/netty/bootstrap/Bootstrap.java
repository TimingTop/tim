package com.timing.im.netty.bootstrap;

import com.timing.im.netty.channel.Channel;

import java.net.SocketAddress;

public class Bootstrap extends AbstractBootstrap<Bootstrap, Channel> {

    private SocketAddress remoteAddress;

    public void connect(String host, int port) {
        SocketAddress remoteAddress = this.remoteAddress;

         doResolveAndConnect(remoteAddress);
    }

    public void doResolveAndConnect(SocketAddress remoteAddress) {



    }

    public void doResolveAndConnect0() {

    }
}
