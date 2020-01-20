package com.sun.quickstart;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * @create: 2020-01-17 16:04
 */
public class Main {

    public static void main(String[] args) {
        Disruptor<MutableObject> disruptor = new Disruptor<>(
                () -> new MutableObject(),
                1024 * 1024, r -> {
            Thread thread = new Thread(r);
            thread.setName("hhhh");
            return thread;
        }, ProducerType.SINGLE, new YieldingWaitStrategy());


        disruptor.handleEventsWith((EventHandler<MutableObject>) (event, sequence, endOfBatch) -> System.out.println("我是消费者，消费的数据为：" + event.toString() + " , sequence 为 " + sequence + ", end 为" + endOfBatch));


        disruptor.start();


        RingBuffer<MutableObject> ringBuffer = disruptor.getRingBuffer();
        long next = ringBuffer.next();
        MutableObject mutableObject = ringBuffer.get(next);
        mutableObject.setObject("xyz");
        ringBuffer.publish(next);


        ringBuffer.publishEvent((event, sequence) -> event.setObject("abc"));
        disruptor.publishEvent((event, sequence) -> event.setObject("opq"));


        EventProduce eventProduce = new EventProduce(ringBuffer);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        LongStream.range(0, 10).forEach(i -> {
            byteBuffer.putLong(0, i);
            eventProduce.setDate(byteBuffer);
        });


        EventProduceByTranslator translator = new EventProduceByTranslator(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        IntStream.range(0, 5).forEach(i -> {
            bb.putDouble(0, i);
            translator.setDate(bb);
        });

        disruptor.shutdown();
    }
}
