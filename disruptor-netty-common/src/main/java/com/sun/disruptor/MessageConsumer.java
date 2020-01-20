package com.sun.disruptor;

import com.lmax.disruptor.WorkHandler;
import lombok.*;

/**
 * @create: 2020-01-19 17:21
 */


@NoArgsConstructor
@Getter
@Setter
public abstract class MessageConsumer implements WorkHandler<MutableObject> {

    protected String id;

    public MessageConsumer(String id) {
        this.id = id;
    }
}
