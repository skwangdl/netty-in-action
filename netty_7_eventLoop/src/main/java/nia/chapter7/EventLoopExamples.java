package nia.chapter7;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class EventLoopExamples {
    /**
     * Listing 7.1 Executing tasks in an event loop
     * */
    @Test
    public void executeTaskInEventLoop() {
        boolean terminated = true;
        while (!terminated) {
            List<Runnable> readyEvents = blockUntilEventsReady();
            for (Runnable ev: readyEvents) {
                ev.run();
            }
        }
    }

    private static final List<Runnable> blockUntilEventsReady() {
        return Collections.<Runnable>singletonList(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " start...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
