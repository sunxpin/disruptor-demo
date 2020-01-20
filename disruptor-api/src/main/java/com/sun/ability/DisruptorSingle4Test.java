package com.sun.ability;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @create: 2020-01-19 15:43
 */
public class DisruptorSingle4Test {

    public static void main(String[] args) {

        Disruptor<TestData> disruptor = new Disruptor<>(() -> new TestData(), 1024 * 1024, r -> {
            return new Thread(r);
        }, ProducerType.SINGLE, new YieldingWaitStrategy());


        disruptor.handleEventsWith(new TestDataHandler());
        disruptor.start();

        RingBuffer<TestData> ringBuffer = disruptor.getRingBuffer();
        long i = 0;
        while (i < Constants.MAX_NUM_FM) {
            long next = ringBuffer.next();
            TestData testData = ringBuffer.get(next);
            testData.setId(i);
            testData.setName("c:" + i);
            ringBuffer.publish(next);
            i++;
        }

    }
}
