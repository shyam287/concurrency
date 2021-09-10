package producerconsumer.waitnotify;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        Queue queue = new LinkedList();


        ExecutorService ec = Executors.newFixedThreadPool(2);
        ec.execute(new Producer(queue));
        ec.execute(new Consumer(queue));

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
