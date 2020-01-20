package com.sun.client;

import com.sun.disruptor.MessageProducer;
import com.sun.disruptor.Order;
import com.sun.disruptor.RingBufferWorkPoolFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
    	/**
    	try {
    		TranslatorData response = (TranslatorData)msg;
    		System.err.println("Client端: id= " + response.getId() 
    				+ ", name= " + response.getName()
    				+ ", message= " + response.getMessage());
		} finally {
			//一定要注意 用完了缓存 要进行释放
			ReferenceCountUtil.release(msg);
		}
		*/
		Order response = (Order)msg;
		String produceId = "client sessionId :1";
		MessageProducer messageProducer = RingBufferWorkPoolFactory.producers.get(produceId);
		messageProducer.publish(msg, ctx);
    	
    	
    }
}
