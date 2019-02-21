package com.linlazy.mmorpg.client;

import com.linlazy.mmorpg.client.command.LoginCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * websocket客戶端
 * @author linlazy
 */
@Slf4j
public class WebSocketClient {

    private static NioEventLoopGroup workGroup = new NioEventLoopGroup(16);

    public static void main(String[] args) {


        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture channelFuture = bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpClientCodec());
                        ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024 * 10));
                        ch.pipeline().addLast(new WebSocketClientHandler());
                    }
                })
                .connect("127.0.0.1", 41235);

        try {
            URI websockeURI = new URI("ws://localhost:41235/websocket");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        channelFuture.addListener(future -> {
            if(future.isSuccess()){
                log.info("建立连接成功");
                startConsoleThread(channelFuture.channel());
            }else {

                log.error("建立连接失败",future.cause());
            }
        });


    }

    private static void startConsoleThread(Channel channel) {
        Scanner scanner = new Scanner(System.in);
        LoginCommand loginCommand = new LoginCommand();
        new Thread(()->{
            String next = scanner.next();
            channel.writeAndFlush(next);

        }).start();
    }
}
