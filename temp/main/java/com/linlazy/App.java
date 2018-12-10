package com.linlazy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;

/**
 *
 * 启动类
 */
public class App {

    private static final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private static final NioEventLoopGroup workGroup = new NioEventLoopGroup();
    private static int port = 41235;

    public static void main( String[] args ) {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
       doBind(serverBootstrap,port);
    }

    private static void doBind(ServerBootstrap serverBootstrap, int port) {
        ChannelFuture channelFuture = serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new IdleStateHandler(0,0,10));
                        socketChannel.pipeline().addLast(new HttpServerCodec());
                        socketChannel.pipeline().addLast(new HttpObjectAggregator(65535));
                    }
                }).bind(port);

        channelFuture.addListener(future -> {
            if(future.isSuccess()) {
                System.out.println(String.format("bind port [%d] success",port));
            }else {
                doBind(serverBootstrap,port + 1);
            }
        });
    }
}
