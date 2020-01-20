package com.sun;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.sun.client.ClientHandler;
import com.sun.client.MessageConsumer4Client;
import com.sun.codes.MarshallingCodeCFactory;
import com.sun.disruptor.MessageConsumer;
import com.sun.disruptor.RingBufferWorkPoolFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NettyClientApp {
    @Bean
    public EventLoopGroup eventLoopGroup() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(new DefaultThreadFactory("worker"));
        return eventLoopGroup;
    }

    public static void main(String[] args) {
        SpringApplication springApplication =
                new SpringApplicationBuilder()
                        .web(WebApplicationType.NONE)
                        .sources(NettyClientApp.class)
                        .build();

        ConfigurableApplicationContext ctx = springApplication.run(args);


        MessageConsumer[] messageConsumer = new MessageConsumer4Client[10];
        for (int i = 0; i < messageConsumer.length; i++) {
            messageConsumer[i] = new MessageConsumer4Client("CLIENT id : " + i);
        }
        new RingBufferWorkPoolFactory.Builder()
                .producerType(ProducerType.SINGLE)
                .bufferSize(1024 * 1024)
                .waitStrategy(new BlockingWaitStrategy())
                .messageConsumers(messageConsumer)
                .build()
                .start();

        EventLoopGroup workGroup = ctx.getBean(EventLoopGroup.class);

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                        sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });
        try {
            // 绑定端口，同步等等请求连接
            ChannelFuture future = bootstrap.connect("localhost", 8352).sync();
            System.err.println("Client connected...");

            // 接下来就进行数据的发送, 但是首先我们要获取channel:

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
