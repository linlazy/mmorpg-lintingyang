package com.linlazy.game.client;

import com.linlazy.game.client.command.console.ConsoleCommandManager;
import com.linlazy.game.client.command.console.LoginConsoleCommand;
import com.linlazy.game.client.repsonse.LoginResponseHandler;
import com.linlazy.game.common.SessionUtil;
import com.linlazy.game.protocol.code.PacketDecoder;
import com.linlazy.game.protocol.code.Spliter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {

    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
    private static final String host = "127.0.0.1";
    private static final int port = 41235;
    private static final int MAX_RETRY_TIMES = 5;

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(nioEventLoopGroup);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new Spliter(Integer.MAX_VALUE));
                socketChannel.pipeline().addLast(new PacketDecoder());
                socketChannel.pipeline().addLast(new LoginResponseHandler());
            }
        });

        connect(bootstrap,host,port,MAX_RETRY_TIMES);
    }

    private static void connect(Bootstrap bootstrap,String host,int port,int remainRetryTimes){
        bootstrap.connect(host,port).addListener(future -> {
            //连接成功
            if(future.isSuccess()){
                System.out.println("连接服务器成功");
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);

            //重连失败
            }else if(remainRetryTimes == 0){
                System.err.println("重连失败，达到最大重连次数");

                //重连
            } else {
                //重连次数
                int order = (MAX_RETRY_TIMES - remainRetryTimes) + 1;
                int delay = 1 << order;
                bootstrap.config().group().schedule(() ->{
                    connect(bootstrap,host,port,remainRetryTimes - 1);
                },delay, TimeUnit.SECONDS);
            }
        });
    }

    private static void startConsoleThread(Channel channel){

        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
        ConsoleCommandManager consoleCommandManager =  new ConsoleCommandManager();

        Scanner scanner = new Scanner(System.in);

        new Thread(() ->{
            while(!Thread.interrupted()){
                //是否登录
                if(!SessionUtil.hasLogin(channel)){
                    loginConsoleCommand.exec(scanner,channel);
                }else {
                    System.out.println("请输入指令");
                    consoleCommandManager.exec(scanner,channel);
                }
            }
        }).start();
    }

}
