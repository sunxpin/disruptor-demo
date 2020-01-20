package com.sun.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * @create: 2020-01-19 17:31
 */
public class RingBufferWorkPoolFactory {

    private ProducerType producerType;

    private WaitStrategy waitStrategy;

    private int bufferSize;

    private RingBuffer<MutableObject> ringBuffer;

    private SequenceBarrier sequenceBarrier;

    private MessageConsumer[] messageConsumers;

    private WorkerPool<MutableObject> workerPool;

    public static Map<String, MessageConsumer> consumers = new ConcurrentHashMap<>();

    public static Map<String, MessageProducer> producers = new ConcurrentHashMap<>();


    public RingBufferWorkPoolFactory(Builder builder) {
        this.producerType = builder.producerType;
        this.bufferSize = builder.bufferSize;
        this.waitStrategy = builder.waitStrategy;
        this.messageConsumers = builder.messageConsumers;


    }

    public void start() {
        // 1. 构建ringBuffer对象
        this.ringBuffer = RingBuffer.create(this.producerType,
                () -> new MutableObject(),
                this.bufferSize,
                this.waitStrategy);

        // 2.设置序号栅栏
        this.sequenceBarrier = this.ringBuffer.newBarrier();

        // 3.设置工作池
        this.workerPool = new WorkerPool<>(this.ringBuffer,
                this.sequenceBarrier,
                new EventExceptionHandler(), this.messageConsumers);

        // 4.把所构建的消费者置入池中
        for (MessageConsumer ms : this.messageConsumers) {
            consumers.put(ms.getId(), ms);
        }

        // 5.缓存生产者
        producers.put("server sessionId :1", new MessageProducer("server sessionId :1", this.ringBuffer));
        producers.put("client sessionId :1", new MessageProducer("client sessionId :1", this.ringBuffer));


        // 6.添加我们的sequences
        this.workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2));

        // 7.启动我们的工作池
        this.ringBuffer.addGatingSequences(this.workerPool.getWorkerSequences());
    }

    public MessageProducer getProducer(String producerId) {
        MessageProducer messageProducer = this.producers.get(producerId);
        if (messageProducer == null) {
            messageProducer = new MessageProducer(producerId, this.ringBuffer);
            producers.put(producerId, messageProducer);
        }
        return messageProducer;
    }

    public static class Builder {

        ProducerType producerType;

        WaitStrategy waitStrategy;

        int bufferSize;

        MessageConsumer[] messageConsumers;

        public Builder producerType(ProducerType producerType) {
            this.producerType = producerType;
            return this;
        }

        public Builder waitStrategy(WaitStrategy waitStrategy) {
            this.waitStrategy = waitStrategy;
            return this;
        }

        public Builder bufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }

        public Builder messageConsumers(MessageConsumer[] messageConsumers) {
            this.messageConsumers = messageConsumers;
            return this;
        }

        public RingBufferWorkPoolFactory build() {
            return new RingBufferWorkPoolFactory(this);
        }
    }


    private static class EventExceptionHandler implements ExceptionHandler<MutableObject> {
        @Override
        public void handleEventException(Throwable ex, long sequence, MutableObject event) {

        }

        @Override
        public void handleOnStartException(Throwable ex) {

        }

        @Override
        public void handleOnShutdownException(Throwable ex) {

        }
    }
}
