package com.sun;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.sun.codes.MarshallingCodeCFactory;
import com.sun.disruptor.MessageConsumer;
import com.sun.disruptor.RingBufferWorkPoolFactory;
import com.sun.server.MessageConsumer4Server;
import com.sun.server.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
public class NettyServerApp {


    @Bean
    public EventLoopGroup eventLoopGroup() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(new DefaultThreadFactory("worker"));
        return eventLoopGroup;
    }

    public static void main(String[] args) {
        SpringApplication springApplication =
                new SpringApplicationBuilder()
                        .web(WebApplicationType.NONE)
                        .sources(NettyServerApp.class)
                        .build();


        ConfigurableApplicationContext ctx = springApplication.run(args);


        MessageConsumer[] messageConsumer = new MessageConsumer4Server[10];
        for (int i = 0; i < messageConsumer.length; i++) {
            messageConsumer[i] = new MessageConsumer4Server("SERVER id : " + i);
        }

        new RingBufferWorkPoolFactory.Builder()
                .producerType(ProducerType.SINGLE)
                .bufferSize(1024 * 1024)
                .waitStrategy(new BlockingWaitStrategy())
                .messageConsumers(messageConsumer)
                .build()
                .start();

        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
        EventLoopGroup workGroup = ctx.getBean(EventLoopGroup.class);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(MarshallingCodeCFactory.buildMarshallingDecoder())
                                .addLast(MarshallingCodeCFactory.buildMarshallingEncoder())
                                .addLast(new ServerHandler());
                    }
                });
        try {
            ChannelFuture fu = bootstrap.bind(8352).sync();
            System.err.println("Server Started...");
            fu.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
