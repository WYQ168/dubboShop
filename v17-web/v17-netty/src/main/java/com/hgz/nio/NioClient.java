package com.hgz.nio;
import	java.nio.ByteBuffer;
import	java.net.InetSocketAddress;
import java.io.IOException;
import java.nio.ByteBuffer;
import	java.nio.channels.SocketChannel;

public class NioClient {
    public static void main(String[] args) throws IOException {
        //创建一个网络通道
        SocketChannel channel = SocketChannel.open();
        //设置该通道为非阻塞
        channel.configureBlocking(false);
        //设置连接的服务IP和端口号
        InetSocketAddress address = new InetSocketAddress("127.0.0.1",6666);
        //连接服务器
        if(!channel.connect(address)){
            //连接不成功
            while(!channel.finishConnect()){
                //等待连接，体现NIO的非阻塞优势
                System.out.println("客户端在连接其他服务端的同时，还 可以做其他的事情");
            }
        }
        //创建一个缓冲区，用于存放发送的数据
        ByteBuffer buffer = ByteBuffer.wrap("hello,nio server".getBytes());
        //发送数据
        channel.write(buffer);
        //避免服务端程序结束，设置阻塞
        System.in.read();
    }
}
