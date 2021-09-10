package producerconsumer.lock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) {

        Queue queue = new LinkedList();
        Lock lock = new ReentrantLock();
        Condition notFull = lock.newCondition();
        Condition notEmpty = lock.newCondition();

        new Producer(queue, lock, notFull, notEmpty);
        ExecutorService ec = Executors.newFixedThreadPool(2);
        ec.execute(new Producer(queue, lock, notFull, notEmpty));
        ec.execute(new Consumer(queue, lock, notFull, notEmpty));

        ec.shutdown();
        try {
            ec.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            ec.shutdownNow();
        }

    }
}
