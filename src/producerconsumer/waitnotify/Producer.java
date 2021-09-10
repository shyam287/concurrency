package producerconsumer.waitnotify;

import java.util.Queue;

public class Producer implements Runnable {

    Queue queue;

    public Producer(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        System.out.println("producer run called ");
        for (int i= 0 ; i<= 10; i++) {
            synchronized (queue) {
                while (queue.size() == 10) {
                    try {
                        System.out.println("producer thread waiting " + Thread.currentThread().getName());
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("produced value " + i);
                queue.add(i);
                queue.notify();
            }
        }
    }
}
