package com.timing.im.connect.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetAddress;

public class NettyConnectionAcceptor {

    private InetAddress bindAddress;
    private int defaultPort;

    EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;

    ServerBootstrap b;

    public NettyConnectionAcceptor(InetAddress bindAddress, int defaultPort) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))

                // 此 方法就是 mina 的 ioSession 功能，没一个 socket 连接会 new一个 handler 来保存 socket 的上下文，
                // 这个是针对每一个 socket 的，handler 是new 出来的，保持唯一性
                //
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new EchoServerHandler());
                    }
                });
        this.bindAddress = bindAddress;
        this.defaultPort = defaultPort;


        // todo
        // 根据不同的类型，创建 不同的 handler 来出来 处理信息包






    }


    public void start() {
//        Channel channel = b.bind(8899).channel();
        try {

            ChannelFuture f = b.bind(bindAddress, defaultPort).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop() {

    }
}
