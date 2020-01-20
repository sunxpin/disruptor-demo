package com.sun.quickstart;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @create: 2020-01-19 14:44
 */
public class EventProduceByTranslator {

    private RingBuffer<MutableObject> ringBuffer;

    private static final EventTranslatorOneArg<MutableObject, ByteBuffer> TRANSLATOR = (event, sequence, bb) -> event.setObject(bb.getDouble(0));

    public EventProduceByTranslator(RingBuffer<MutableObject> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void setDate(ByteBuffer bb) {
        ringBuffer.publishEvent(TRANSLATOR, bb);
    }
}
