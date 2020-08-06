package com.timing.im.network.connector;

import com.timing.im.network.endpoint.RawData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TcpNettyServerConnector implements Connector {

    private static final AtomicInteger THREAD_COUNTER = new AtomicInteger();
    private static final ThreadGroup TCP_THREAD_GROUP = new ThreadGroup("tcp-netty-server");

    static {
        TCP_THREAD_GROUP.setDaemon(false);
    }

    private InetSocketAddress localAddress;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;


    // 复用 连接
    private ConcurrentMap<SocketAddress, Channel> activeChannels = new ConcurrentHashMap<>();

    @Override
    public synchronized void start() throws Exception {
        int id = THREAD_COUNTER.incrementAndGet();
        // reactor 模型中，只用来做 socket 的accept 事件
        bossGroup = new NioEventLoopGroup(1);

        workerGroup = new NioEventLoopGroup(5);

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(null)
                .option(ChannelOption.SO_BACKLOG, 100)
                .childOption(ChannelOption.SO_KEEPALIVE, true);


        ChannelFuture channelFuture = bootstrap.bind(localAddress).syncUninterruptibly();
    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    // 发送消息，找到 跟 client 连接的channel
    public void send(final RawData msg) {
        // tcp 不支持 广播
        // rawData 应该有个响应的地址可以查询到
        Channel channel = activeChannels.get("");

        channel.writeAndFlush(Unpooled.wrappedBuffer(msg.getBytes()));

    }

    private class ChannelRegistry extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {

            ch.pipeline().addLast(new ChannelTracker());
        }
    }

    private class ChannelTracker extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            // 根据远程 client 的地址，保存对应的 channel，复用 连接
            activeChannels.put(ctx.channel().remoteAddress(), ctx.channel());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            activeChannels.remove(ctx.channel().remoteAddress());
        }
    }
}
