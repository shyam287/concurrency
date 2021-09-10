package producerconsumer.lock;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Consumer implements Runnable {

    Queue queue;
    Lock lock;
    Condition notFull;
    Condition notEmpty;

    public Consumer(Queue queue, Lock lock, Condition notFull, Condition notEmpty) {
        this.queue = queue;
        this.lock = lock;
        this.notFull = notFull;
        this.notEmpty = notEmpty;
    }

    @Override
    public void run() {

        System.out.println("consumer run called ");
        while (true) {
            lock.lock();
                while (queue.isEmpty()) {
                    try {
                        System.out.println("consumer thread waiting " + Thread.currentThread().getName());
                        notEmpty.await();
                    } catch (InterruptedException e) {
                        System.out.println("thread interrupted " + Thread.currentThread().getName());
                        break;
                    }
                }
                System.out.println("consumed value " + queue.remove());
                notFull.signal();

            lock.unlock();
        }

    }
}
