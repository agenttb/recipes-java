package com.bintian.learn.tire;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockQueue {

    private volatile int capacity;
    private ReentrantLock lock;
    private Condition takeCond;
    private Condition putCond;

    public BlockQueue(int capacity) {
        this.capacity = capacity;
        lock = new ReentrantLock();
        takeCond = lock.newCondition();
        putCond = lock.newCondition();
        this.queue = new LinkedList<>();
    }
    private LinkedList<Integer> queue;

    public void put(Integer value) {
        try {
            lock.lock();
            while (queue.size() > capacity) {
                putCond.await();

            }
            queue.addFirst(value);
            takeCond.signalAll();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public  Integer take() {
        try {
            lock.lock();
            while (queue.isEmpty()) {
                takeCond.await();
            }
            var value = queue.pollLast();
            putCond.signalAll();
            return value;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        BlockQueue blockQueue = new BlockQueue(10);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                blockQueue.put(i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                var value = blockQueue.take();
                System.out.println("take value : " + value);
            }
        });
        t1.start();
        t2.start();
    }
}
