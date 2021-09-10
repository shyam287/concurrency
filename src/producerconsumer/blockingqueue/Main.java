package producerconsumer.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) {

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        ExecutorService ec = Executors.newFixedThreadPool(8);
        ec.execute(new Producer(queue));
        ec.execute(new Consumer(queue));

        ec.shutdown();
        try {
            ec.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            ec.shutdownNow();
        }

    }
}
