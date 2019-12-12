package com.hgz.v17nettyspringboot.ws;
import	java.awt.TrayIcon.MessageType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgz.v17nettyspringboot.pojo.Message;
import com.hgz.v17nettyspringboot.utils.ChannelUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@ChannelHandler.Sharable  //设置handler在channel中共享
public class WSHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

/*
 //1.创建对象，管理所有的客户端channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
*/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        //1.获取客户端传过来的消息
        String text = msg.text();
        System.out.println("接收到的消息："+text);
       /*  //2.发送消息给其他服务器
       clients.writeAndFlush(new TextWebSocketFrame(LocalDateTime.now()+"接受到的消息为："+text));  */

       //转换为对象
        ObjectMapper objectMapper = new ObjectMapper();
        Message message= objectMapper.readValue(text, Message.class);
        if("1".equals(message.getMsgType())){
            log.info("{},发来连接请求",ctx.channel().remoteAddress());
            String userId = (String) message.getData();
            log.info("用户id为{}",userId);
            //把当前用户和通道的映射关系保存起来
            ChannelUtils.add(userId,ctx.channel());
            log.info(" {}和{}建立了映射关系", userId,ctx.channel());
        }
        ctx.fireChannelRead(message);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/*        clients.add(ctx.channel());*/
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //当触发handlerRemoved时，ChannelGroup会移除对应的客户端channel
        // 所以此处可以不写代码
        /*System.out.println(ctx.channel()+":断开连接");*/
    }



}
