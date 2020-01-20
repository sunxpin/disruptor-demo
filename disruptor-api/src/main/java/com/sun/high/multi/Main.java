package com.sun.high.multi;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @create: 2020-01-19 16:30
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        // 1 创建RingBuffer
        RingBuffer<Order> ringBuffer =
                RingBuffer.create(ProducerType.MULTI,
                        () -> new Order(),
                        1024 * 1024,
                        new YieldingWaitStrategy());

        // 2 通过ringBuffer 创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        // 3 创建多个消费者数组:
        Consumer[] consumers = new Consumer[10];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer("C" + i);
        }

        // 4 构建多消费者工作池
        WorkerPool<Order> workerPool = new WorkerPool<>(
                ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                consumers);

        // 5 设置多个消费者的sequence序号 用于单独统计消费进度, 并且设置到ringbuffer中
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        // 6 启动workerPool
        workerPool
                .start(Executors.newFixedThreadPool(5));

        final CountDownLatch latch = new CountDownLatch(1);

        final Producer producer = new Producer(ringBuffer);
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    latch.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < 100; j++) {
                    producer.sendData(UUID.randomUUID().toString());
                }
            }).start();
        }

        Thread.sleep(2000);
        System.err.println("线程创建完毕，开始生产数据");
        latch.countDown();

        Thread.sleep(10000);

        System.err.println("任务总数:" + consumers[2].getCount());
    }

    static class EventExceptionHandler implements ExceptionHandler<Order> {
        public void handleEventException(Throwable ex, long sequence, Order event) {
        }

        public void handleOnStartException(Throwable ex) {
        }

        public void handleOnShutdownException(Throwable ex) {
        }
    }


}
