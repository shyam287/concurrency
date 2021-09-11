package countdownlatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(4);

        Runnable runnable = getRunnable(latch);

        Callable<String> callable = getCallable(latch);

        ExecutorService ec = Executors.newFixedThreadPool(4);


        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            futures.add((Future<String>) ec.submit(runnable));
            futures.add(ec.submit(callable));
        }

        System.out.println("before latch count " + latch.getCount());
        latch.await();
        System.out.println("after latch count " + latch.getCount());
        printFutures(futures);

        shutdownThreads(ec);

    }

    private static void printFutures(List<Future<String>> futures) {
        for(Future<String> future: futures) {
            try {
                System.out.println("output future "+ future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private static Callable<String> getCallable(CountDownLatch latch) {

        return () -> {
            System.out.println("awaiting for latch to become zero in callable task " + Thread.currentThread().getName());
            Thread.sleep(1000);
            latch.countDown();
            return "completed " + Thread.currentThread().getName();
        };
    }

    private static Runnable getRunnable(CountDownLatch latch) {
        return () -> {

            System.out.println("awaiting for latch to become zero in runnable task " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        };
    }

    private static void shutdownThreads(ExecutorService ec) {

        ec.shutdown();
        try {
            ec.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(!ec.isShutdown()) {
                ec.shutdownNow();
            }
        }
    }
}
