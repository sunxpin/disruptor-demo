package com.sun.server;

import com.sun.disruptor.MessageConsumer;
import com.sun.disruptor.MutableObject;
import com.sun.disruptor.Order;
import io.netty.channel.ChannelHandlerContext;

/**
 * @create: 2020-01-20 08:53
 */
public class MessageConsumer4Server extends MessageConsumer {

    public MessageConsumer4Server(String id) {
        super(id);
    }

    @Override
    public void onEvent(MutableObject event) {

        Order Order = ((Order) event.getObject());
        ChannelHandlerContext ctx = event.getCtx();
        // 1.业务处理逻辑:
        System.err.println("Sever端: id= " + Order.getId()
                + ", name= " + Order.getName()
                + ", message= " + Order.getMessage());


        // 2.回送响应信息:
        Order response = new Order();
        response.setId("resp: " + Order.getId());
        response.setName("resp: " + Order.getName());
        response.setMessage("resp: " + Order.getMessage());

        // 3.写出response响应信息:
        ctx.writeAndFlush(response);
    }
}
