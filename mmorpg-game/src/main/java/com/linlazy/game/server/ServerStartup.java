package com.linlazy.game.server;

import com.linlazy.game.LoginRequestHandler;
import com.linlazy.game.protocol.code.PacketDecoder;
import com.linlazy.game.protocol.code.Spliter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Hello world!
 *
 */
public class ServerStartup { private static final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private static final NioEventLoopGroup workGroup = new NioEventLoopGroup();
    private static int port = 41235;

    public static void main( String[] args ) {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        doBind(serverBootstrap,port);
    }

    private static void doBind(final ServerBootstrap serverBootstrap, final int port) {
        ChannelFuture channelFuture = serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new Spliter(Integer.MAX_VALUE));
                        socketChannel.pipeline().addLast(new PacketDecoder());
                        socketChannel.pipeline().addLast(new LoginRequestHandler());
                    }
                }).bind(port);

        channelFuture.addListener(future -> {
            if(future.isSuccess()){
                System.out.println(String.format("bind port [%d] success",port));
            }else {
                doBind(serverBootstrap,port + 1);
            }
        });
    }
}
