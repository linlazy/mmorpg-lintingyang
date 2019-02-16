import com.linlazy.mmorpg.server.threadpool.ScheduledThreadPool;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author linlazy
 */
public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {

        ScheduledFuture<?> schedule =new ScheduledThreadPool(1).scheduleWithFixedDelay(() -> {
            System.out.println("before");
            System.out.println("after");
        },0L, 2L, TimeUnit.SECONDS);
        schedule.cancel(true);



    }
}