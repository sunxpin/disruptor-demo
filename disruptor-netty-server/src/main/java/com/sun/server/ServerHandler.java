package com.sun.server;

import com.sun.disruptor.MessageProducer;
import com.sun.disruptor.RingBufferWorkPoolFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @create: 2020-01-20 08:55
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) {
        String produceId = "server sessionId :1";
        MessageProducer messageProducer = RingBufferWorkPoolFactory.producers.get(produceId);
        messageProducer.publish(o, channelHandlerContext);
    }
}
