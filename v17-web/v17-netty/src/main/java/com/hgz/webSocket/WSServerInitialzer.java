package com.hgz.webSocket;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


public class WSServerInitialzer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //1.获取通道对象
        ChannelPipeline pipeline = channel.pipeline();
        //2.设置http编码器
        pipeline.addLast(new HttpServerCodec());
        //3.考虑到传输数据，设置支持大数据流
        pipeline.addLast(new ChunkedWriteHandler());
        //4.对HTTPMessage做聚合,设置支持传输的最大长度为1024*32 字节
        pipeline.addLast(new HttpObjectAggregator(1024*32));
        //5,设置跟websocket相关的设置
        // 用于指定给客户端连接访问的路由 : /ws
        // 对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //6,设置自定义handler
        pipeline.addLast(new WSHandler());

    }
}
