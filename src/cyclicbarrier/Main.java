package cyclicbarrier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {

        CyclicBarrier barrier = new CyclicBarrier(4);

        Runnable runnable = getRunnable(barrier);

        Callable<String> callable = getCallable(barrier);

        ExecutorService ec = Executors.newFixedThreadPool(4);
        List<Future<String>> futures = new ArrayList<>();

        for(int j=0; j< 4; j++) {

            for (int i = 0; i < 2; i++) {
                futures.add((Future<String>) ec.submit(runnable));
                futures.add(ec.submit(callable));
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("printing for iteration: " + j);
            printFutures(futures);
            if (barrier.getNumberWaiting() == 0) {
                barrier.reset();
            }
        }
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

    private static Callable<String> getCallable(CyclicBarrier barrier) {

        return () -> {
            System.out.println("awaiting for barrier to open in callable task " + Thread.currentThread().getName());
            barrier.await();
            System.out.println("barrier opened " + Thread.currentThread().getName());
            return "completed " + Thread.currentThread().getName();
        };
    }

    private static Runnable getRunnable(CyclicBarrier barrier) {
        return () -> {

            System.out.println("awaiting for barrier in runnable task " + Thread.currentThread().getName());
            try {
                barrier.await(1, TimeUnit.MILLISECONDS); // added to simulate BrokenBarrierException
                System.out.println("barrier opened " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException | TimeoutException e) {
                e.printStackTrace();
            }
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
