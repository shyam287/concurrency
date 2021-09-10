package producerconsumer.waitnotify;

import java.util.Queue;

public class Consumer implements Runnable {

    Queue queue;

    public Consumer(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        System.out.println("consumer run called ");
        while (true) {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        System.out.println("consumer thread waiting " + Thread.currentThread().getName());
                        queue.wait();
                    } catch (InterruptedException e) {
                        System.out.println("thread interrupted " + Thread.currentThread().getName());
                        break;
                    }
                }
                System.out.println("consumed value " + queue.remove());
                queue.notify();
            }
        }
    }
}
