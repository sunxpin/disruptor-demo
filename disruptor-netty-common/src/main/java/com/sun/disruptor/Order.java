package com.sun.disruptor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @create: 2020-01-20 10:24
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 876356128619901881L;

    private String id;
    private String name;
    private String message;    //传输消息体内容
}
