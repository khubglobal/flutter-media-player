package com.easternblu.khub.common.util;

import com.easternblu.khub.common.CommonTest;
import com.easternblu.khub.common.util.throttle.ThreadSafeThrottle;
import com.easternblu.khub.common.util.throttle.Throttle;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by pan on 31/3/17.
 */

public class ThrottleLockTest extends CommonTest {

    volatile int taskDone = 0, taskSkipped = 0, tasksTotal = 0;
    volatile boolean running = false;

    @Test
    public void testThrottle() {
        final int COUNT_TO = 100;
        log("begin");
        running = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                count(1, COUNT_TO, new Action() {
                    @Override
                    public void performOn(int value) {
                        checkNumber(value);
                    }
                });
                running = false;
            }
        }).start();

        while (running)
            uninterruptableSleep(1000);
        uninterruptableSleep(10000);
        log("taskDone = " + taskDone);
        log("taskSkipped = " + taskSkipped);
        log("tasksTotal = " + tasksTotal);

        assertTrue(COUNT_TO + " requests received", tasksTotal == COUNT_TO);
        assertTrue("Most requests skipped", taskSkipped > taskDone);

    }


    Throttle throttle = new ThreadSafeThrottle(8, 2000) {
        @Override
        protected Long getNow() {
            return System.currentTimeMillis();
        }
    };

    private void checkNumber(final int num) {

        tasksTotal++;
        if (!throttle.acquireSlot()) {
            taskSkipped++;
            //    log("cannot aquire. throw away " + num);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                log("got lock, performing action " + num);
                //uninterruptableSleep(2000);
                taskDone++;
                log(">>>>> slot freed " + num);
            }
        }).start();

    }


    private static void uninterruptableSleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        }
    }

    private void count(final int from, final int to, final Action action) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = from; i <= to; i++) {
                    uninterruptableSleep(100);
                    // log("count num=" + num);
                    action.performOn(i);
                }
            }
        }).start();
    }

    private interface Action {
        public void performOn(int value);
    }

}
