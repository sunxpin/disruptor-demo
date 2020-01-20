package com.sun.disruptor;

import com.lmax.disruptor.RingBuffer;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @create: 2020-01-19 17:20
 */
@Slf4j
public class MessageProducer {

    private String id;

    private RingBuffer<MutableObject> ringBuffer;

    public MessageProducer(String id, RingBuffer<MutableObject> ringBuffer) {
        this.id = id;
        this.ringBuffer = ringBuffer;
    }

    public void publish(Object object, ChannelHandlerContext channelHandlerContext) {
        log.error("producerId:{}", this.id);
        long next = this.ringBuffer.next();
        try {
            MutableObject mutableObject = ringBuffer.get(next);
            mutableObject.setObject(object);
            mutableObject.setCtx(channelHandlerContext);
        } finally {
            this.ringBuffer.publish(next);
        }

    }
}
