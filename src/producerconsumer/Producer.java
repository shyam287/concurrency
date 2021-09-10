package producerconsumer;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Producer implements Runnable {

    Queue queue;
    Lock lock;
    Condition notFull;
    Condition notEmpty;

    public Producer(Queue queue, Lock lock, Condition notFull, Condition notEmpty) {
        this.queue = queue;
        this.lock = lock;
        this.notFull = notFull;
        this.notEmpty = notEmpty;
    }

    @Override
    public void run() {

        System.out.println("producer run called ");
        for (int i= 0 ; i<= 10; i++) {
            lock.lock();
                while (queue.size() == 10) {
                    try {
                        System.out.println("producer thread waiting " + Thread.currentThread().getName());
                        notFull.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("produced value " + i);
                queue.add(i);
                notEmpty.signal();
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

            lock.unlock();
        }

    }
}
