package nia.chapter7;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Listing 7.2 Scheduling a task with a ScheduledExecutorService
 *
 * Listing 7.3 Scheduling a task with EventLoop
 *
 * Listing 7.4 Scheduling a recurring task with EventLoop
 *
 * Listing 7.5 Canceling a task using ScheduledFuture
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class ScheduleExamples {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * Listing 7.2 Scheduling a task with a ScheduledExecutorService
     * */
    @Test
    public void schedule() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        ScheduledFuture<?> future = executor.schedule(
            new Runnable() {
            @Override
            public void run() {
                System.out.println("Now it is 3 seconds later");
            }
        }, 3, TimeUnit.SECONDS);
        executor.shutdown();
    }

    /**
     * Listing 7.3 Scheduling a task with EventLoop
     * */
    @Test
    public void scheduleViaEventLoop() {
        Channel ch = CHANNEL_FROM_SOMEWHERE; // get reference from somewhere
        ScheduledFuture<?> future = ch.eventLoop().schedule(
            new Runnable() {
            @Override
            public void run() {
                System.out.println("5 seconds later");
            }
        }, 5, TimeUnit.SECONDS);
    }

    /**
     * Listing 7.4 Scheduling a recurring task with EventLoop
     * */
    @Test
    public void scheduleFixedViaEventLoop() {
        Channel ch = CHANNEL_FROM_SOMEWHERE; // get reference from somewhere
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(
           new Runnable() {
           @Override
           public void run() {
               System.out.println("Run every 5 seconds");
               }
           }, 5, 5, TimeUnit.SECONDS);
    }

    /**
     * Listing 7.5 Canceling a task using ScheduledFuture
     * */
    @Test
    public void cancelingTaskUsingScheduledFuture(){
        Channel ch = CHANNEL_FROM_SOMEWHERE; // get reference from somewhere
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Run every 60 seconds");
                    }
                }, 60, 60, TimeUnit.SECONDS);
        // Some other code that runs...
        boolean mayInterruptIfRunning = false;
        future.cancel(mayInterruptIfRunning);
    }
}
