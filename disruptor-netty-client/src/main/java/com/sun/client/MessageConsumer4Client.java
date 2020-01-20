package com.sun.client;

import com.sun.disruptor.MessageConsumer;
import com.sun.disruptor.MutableObject;
import com.sun.disruptor.Order;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * @create: 2020-01-20 10:57
 */
public class MessageConsumer4Client extends MessageConsumer {
    public MessageConsumer4Client(String id) {
        super(id);
    }

    @Override
    public void onEvent(MutableObject event) {
        Order order = ((Order) event.getObject());
        ChannelHandlerContext ctx = event.getCtx();
        //业务逻辑处理:
        try {
            System.err.println("Client端: id= " + order.getId()
                    + ", name= " + order.getName()
                    + ", message= " + order.getMessage());
        } finally {
            ReferenceCountUtil.release(ctx);
        }
    }
}
