package com.sun;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.sun.client.MessageConsumerImpl4Client;
import com.sun.client.NettyClient;
import com.sun.disruptor.MessageConsumer;
import com.sun.disruptor.RingBufferWorkPoolFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyClient2App {

    public static void main(String[] args) {
        SpringApplication.run(NettyClient2App.class, args);

        MessageConsumer[] messageConsumer = new MessageConsumerImpl4Client[10];
        for (int i = 0; i < messageConsumer.length; i++) {
            messageConsumer[i] = new MessageConsumerImpl4Client("CLIENT id : " + i);
        }
        new RingBufferWorkPoolFactory.Builder()
                .producerType(ProducerType.SINGLE)
                .bufferSize(1024 * 1024)
                .waitStrategy(new BlockingWaitStrategy())
                .messageConsumers(messageConsumer)
                .build()
                .start();

        //建立连接 并发送消息
        new NettyClient().sendData();
    }

}
