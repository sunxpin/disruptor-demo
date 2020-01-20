package com.sun.quickstart;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @create: 2020-01-19 14:24
 */
public class EventProduce {
    private RingBuffer<MutableObject> ringBuffer;

    public EventProduce(RingBuffer<MutableObject> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }


    public void setDate(ByteBuffer byteBuffer) {
        long next = this.ringBuffer.next();
        try {
            MutableObject mutableObject = this.ringBuffer.get(next);
            mutableObject.setObject(byteBuffer.getLong(0));
        } finally {
            this.ringBuffer.publish(next);
        }
    }
}
