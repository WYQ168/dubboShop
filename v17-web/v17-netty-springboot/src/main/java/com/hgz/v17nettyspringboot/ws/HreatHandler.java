package com.hgz.v17nettyspringboot.ws;

import com.hgz.v17nettyspringboot.pojo.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
@Slf4j
public class HreatHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        if("2".equals(message.getMsgType())){
            log.info("{},发来一个心跳包",ctx.channel());
            return;
        }
        //继续传递给下一个处理器
        ctx.fireChannelRead(message);
    }
}
