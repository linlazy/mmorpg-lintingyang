package com.linlazy.mmorpg.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author linlazy
 */
@Component
public class ServerBoot implements CommandLineRunner {
    private static final NioEventLoopGroup BOSS_GROUP = new NioEventLoopGroup();
    private static final NioEventLoopGroup WORK_GROUP = new NioEventLoopGroup();
    private static int port = 41235;

    public static void start() {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
//        doBind(serverBootstrap,port);
        //配置线程
        ChannelFuture channelFuture = serverBootstrap.group(BOSS_GROUP, WORK_GROUP)
                //配置通道
                .channel(NioServerSocketChannel.class)
                //配置tcp参数
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                //初始化通道
                .childHandler(new WebsocketChannelInitializer())
                //绑定端口
                .bind(port);

        channelFuture.addListener(future -> {
            if(future.isSuccess()){
                System.out.println(String.format("bind port [%d] success",port));
            }else {
                System.out.println(String.format("bind port [%d] fail",port));
            }
        });
    }

    @Override
    public void run(String... args) throws Exception {
        start();
    }
}
