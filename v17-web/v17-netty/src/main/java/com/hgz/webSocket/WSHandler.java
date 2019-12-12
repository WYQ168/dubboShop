package com.hgz.webSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;


public class WSHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //1.创建对象，管理所有的客户端channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx ,TextWebSocketFrame msg) throws Exception {

        //1.获取客户端传过来的消息
        String text = msg.text();
        System.out.println("接收到的消息："+text);
        //2.发消息给其他服务器
        clients.writeAndFlush(ctx.channel().remoteAddress()+"接受到的消息 为："+text);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+":连接上了服务器");
        clients.add(ctx.channel());
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //当触发handlerRemoved时，ChannelGroup会移除对应的客户端channel
        // 所以此处可以不写代码
        System.out.println(ctx.channel().remoteAddress()+":断开连接");
        clients.remove(ctx.channel());
    }



}
