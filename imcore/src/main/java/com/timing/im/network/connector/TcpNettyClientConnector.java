package com.timing.im.network.connector;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.SocketAddress;
import java.util.function.BooleanSupplier;

/***
 *
 * 使用 netty 作为 连接中间件 ，
 *
 *  客户端 建立 netty 的连接池，一个 client 可以与多个server 对接，也可以 直接跟 一个 server 建立多个 连接
 *  一般的 客户端跟 服务器只建立 一个连接，
 *
 *
 */
public class TcpNettyClientConnector implements Connector {

    private EventLoopGroup workerGroup;

    // 建立连接池
    private AbstractChannelPoolMap<SocketAddress, ChannelPool> poolMap;


    @Override
    public void start() throws Exception {
        // 创建 reactor 的线程池
        workerGroup = new NioEventLoopGroup();
        poolMap = new AbstractChannelPoolMap<SocketAddress, ChannelPool>() {
            @Override
            protected ChannelPool newPool(SocketAddress socketAddress) {
                Bootstrap bootstrap = new Bootstrap()
                        .group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .option(ChannelOption.AUTO_READ, true)
                        .remoteAddress(socketAddress);
                return new FixedChannelPool(bootstrap,)
            }
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    private class MyChannelPoolHandler extends AbstractChannelPoolHandler {
        private final SocketAddress socketAddress;

        MyChannelPoolHandler(SocketAddress address) {
            this.socketAddress = address;
        }
        @Override
        public void channelCreated(Channel channel) throws Exception {

            channel.pipeline().addLast(new IdleStateHandler(0, 0, 30));

        }
    }
}
