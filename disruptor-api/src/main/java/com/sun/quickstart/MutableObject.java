package com.sun.quickstart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @create: 2020-01-17 16:03
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MutableObject {

    private Object object;

}
