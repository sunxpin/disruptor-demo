package com.sun.client;

import com.sun.disruptor.MessageProducer;
import com.sun.disruptor.Order;
import com.sun.disruptor.RingBufferWorkPoolFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @create: 2020-01-20 10:54
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        for(int i =0; i <10; i++){
            Order request = new Order();
            request.setId("" + i);
            request.setName("请求消息名称 " + i);
            request.setMessage("请求消息内容 " + i);
            ctx.writeAndFlush(request);
        }

        String produceId = "client sessionId :1";
        MessageProducer messageProducer = RingBufferWorkPoolFactory.producers.get(produceId);
        messageProducer.publish(msg, ctx);
    }

}
