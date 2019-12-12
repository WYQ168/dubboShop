package com.hgz.webSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WSServer {

    public static void main(String[] args) throws InterruptedException {
        //1.创建主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //2.创建从线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //3.创建服务启动类对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //4.设置主从线程组
        serverBootstrap.group(bossGroup,workerGroup)
                //5.设置NIO双向通道
                .channel(NioServerSocketChannel.class)
                //6.设置子处理对象
                .childHandler(new WSServerInitialzer());
        //7.开启监听
        serverBootstrap.bind(8888).sync();
        System.out.println("Netty开启服务，监听8888。。。");
    }
}
