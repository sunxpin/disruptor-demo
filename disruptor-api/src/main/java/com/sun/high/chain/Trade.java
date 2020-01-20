package com.sun.high.chain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Trade {

	private String id;
	private String name;
	private double price;
	private AtomicInteger count = new AtomicInteger(0);

	
}
