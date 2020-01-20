package com.sun.high.multi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @create: 2020-01-19 16:33
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	private String id;
	private String name;
	private double price;

}
