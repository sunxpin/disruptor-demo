package com.sun.ability;

import org.springframework.util.StopWatch;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @create: 2020-01-19 15:25
 */
public class ArrayBlockingQueue4Test {
    public static void main(String[] args) {

        ArrayBlockingQueue<TestData> arrayBlockingQueue = new ArrayBlockingQueue<>(50_000_000);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        new Thread(() -> {
            long i = 0;
            while (i < Constants.MAX_NUM_FM) {
                TestData testData = TestData.builder().id(i).name("c:" + i).build();
                try {
                    arrayBlockingQueue.put(testData);
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        new Thread(() -> {
            long i = 0;
            while (i < Constants.MAX_NUM_FM) {
                try {
                    arrayBlockingQueue.take();
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        }).start();
    }
}
