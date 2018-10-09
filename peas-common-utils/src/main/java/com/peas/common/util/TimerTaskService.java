package com.peas.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by peaimage on 2016/5/9.
 */
public class TimerTaskService {

    private TimerTaskService() {
    }

    public static void schedule(Runnable runnable, long initialDelay, long delay) {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(runnable, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

}
