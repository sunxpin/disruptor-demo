package com.sun.ability;

import com.lmax.disruptor.EventHandler;
import org.springframework.util.StopWatch;

/**
 * @create: 2020-01-19 16:05
 */
public class TestDataHandler implements EventHandler<TestData> {

    StopWatch stopWatch;

    public TestDataHandler() {
        stopWatch = new StopWatch();
        stopWatch.start();
    }

    private long i = 0;


    @Override
    public void onEvent(TestData event, long sequence, boolean endOfBatch) throws Exception {
        i++;
        if (i == Constants.MAX_NUM_FM) {
            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        }
    }
}
