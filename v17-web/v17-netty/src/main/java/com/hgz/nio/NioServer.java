package com.hgz.nio;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class NioServer {
    public static void main(String[] args) throws IOException {
        //创建serverSocketChannel对象
        ServerSocketChannel serverSocketChannel  = ServerSocketChannel.open();
        //绑定监听的端口号
        serverSocketChannel.bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //创建一个selector对象
        Selector selector = Selector.open();
        //把ServerSocketChannel对象注册给Selector对象
        //同时关注连接事件
        serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        //selector开始监控
        while(true){
            //每隔一秒钟查看是否有可以处理的事件
            if(selector.select(10000)==0){
                //非阻塞的体现
                System.out.println("当前没有事件需要处理，服务器可以做点其他事");
                continue;
            }
            //获取需要处理的事件，因为可能存在多个客户端的事件需要处理，所以返回的是集合
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            //开始处理事件
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                //根据不同的事件，做不同的处理
                if(key.isAcceptable()){
                    System.out.println("有新的客户端连接......");
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    //将当前的channel注册到Selector上，监听可读事件
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }
                if(key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    //创建一个缓存区对象
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    //将通道的数据读取到缓冲区
                    socketChannel.read(buffer);
                    //输出结果
                    System.out.println(new String(buffer.array()));
                }
                //关键一步
                //需要将当前的key移除，避免重复处理
                keyIterator.remove();
            }
        }
    }
}
