package com.timing.im.network.connector;

import com.timing.im.network.channel.RawDataChannel;
import com.timing.im.network.connector.netty.CloseOnIdleHandler;
import com.timing.im.network.connector.netty.DatagramFramer;
import com.timing.im.network.connector.netty.DispatchHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
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
    private RawDataChannel rawDataChannel;

    // 建立连接池
    private AbstractChannelPoolMap<SocketAddress, ChannelPool> poolMap;


    @Override
    public void start() throws Exception {
        if (rawDataChannel == null) {
            throw new IllegalArgumentException("Cannot start without message handler.");
        }
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
//                return new FixedChannelPool(bootstrap,)
                return null;
            }
        };
    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void setRawDataReceiver(RawDataChannel messageHandler) {
        // 拿到处理 rawData 的 处理器，用来 跟  endpoint 联通，达到 分层目的
        this.rawDataChannel = messageHandler;
    }

    private class MyChannelPoolHandler extends AbstractChannelPoolHandler {
        private final SocketAddress socketAddress;

        MyChannelPoolHandler(SocketAddress address) {
            this.socketAddress = address;
        }
        @Override
        public void channelCreated(Channel channel) throws Exception {
            // 1. 保活心跳包(错错错错)，这个是 判断 channel 是不是还空闲，空闲就关掉
            channel.pipeline().addLast(new IdleStateHandler(0, 0, 30));
            // 2. 关闭空闲的 channel
            channel.pipeline().addLast(new CloseOnIdleHandler());
            // 3. 删除掉 空闲的 channel 线程池
            channel.pipeline().addLast(new RemoveEmptyPoolHandler(poolMap, socketAddress));
            // 4. 重点，根据协议，把整个完整的协议数据包，封装成 rawData 提交给下一个步骤
            channel.pipeline().addLast(new DatagramFramer());
            // 5. 使用 channel 接受数据，
            channel.pipeline().addLast(new DispatchHandler(rawDataChannel));
            // todo: 要不要添加一个 exception 的处理器




        }
    }

    private class RemoveEmptyPoolHandler extends ChannelDuplexHandler {
        private final AbstractChannelPoolMap<SocketAddress, ChannelPool> poolMap;
        private final SocketAddress key;

        RemoveEmptyPoolHandler(AbstractChannelPoolMap<SocketAddress, ChannelPool> poolMap, SocketAddress address) {
            this.poolMap = poolMap;
            this.key = address;
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if (poolMap.remove(key)) {
                // 删除掉
            }
        }
    }
}
