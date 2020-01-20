package com.sun.disruptor;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @create: 2020-01-19 17:18
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MutableObject {

    private Object object;

    private ChannelHandlerContext ctx;
}
