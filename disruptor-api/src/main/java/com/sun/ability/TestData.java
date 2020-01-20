package com.sun.ability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @create: 2020-01-19 15:29
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TestData {
    private Long id;
    private String name;
}
