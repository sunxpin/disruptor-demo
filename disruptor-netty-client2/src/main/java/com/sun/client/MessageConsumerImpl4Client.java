package com.sun.client;

import com.sun.disruptor.MessageConsumer;
import com.sun.disruptor.MutableObject;
import com.sun.disruptor.Order;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class MessageConsumerImpl4Client extends MessageConsumer {

    public MessageConsumerImpl4Client(String consumerId) {
        super(consumerId);
    }

    public void onEvent(MutableObject event) {
        Order response = ((Order) event.getObject());
        ChannelHandlerContext ctx = event.getCtx();
        //业务逻辑处理:
        try {
            System.err.println("Client端: id= " + response.getId()
                    + ", name= " + response.getName()
                    + ", message= " + response.getMessage());
        } finally {
            ReferenceCountUtil.release(response);
        }
    }

}
