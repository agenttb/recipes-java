package com.bintian.learn.languagerelated;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSolution {
    private final Lock lock = new ReentrantLock();

    private final Condition oddCond = lock.newCondition();
    private final Condition evenCond = lock.newCondition();

    private final Object monitor = new Object();

    private  int counter = 0;
    private volatile int cnt = 0;
    private final  int limit = 100;

    private void printOddWithLock() {
        while (cnt < limit) {
            synchronized (monitor) {
                while ((cnt & 1) != 1) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (cnt < limit) {
                    System.out.println("Thread :" + Thread.currentThread().threadId() + " counter : " + cnt);
                    cnt++;
                    monitor.notifyAll();
                }


            }
        }
    }

    private void printEvenWithLock() {
        while (cnt < limit) {
            synchronized (monitor) {
                while ((cnt & 1) == 1) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (cnt < limit) {
                    System.out.println("Thread :" + Thread.currentThread().threadId() + " counter : " + cnt);
                    cnt++;
                    monitor.notifyAll();
                }
            }

        }
    }

    public void printOddWithCond() {
        while (counter < limit) {

            lock.lock();
            try {
                while ((counter & 1) == 1) {
                    oddCond.await();
                }
                if (counter < limit) {
                    System.out.println("Thread :" + Thread.currentThread().threadId() + " counter : " + counter);
                    counter++;
                    evenCond.signal();
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public void printEvenWithCond() {
        while (counter < limit) {
            lock.lock();
            try {
                while ((counter &1) != 1) {
                    evenCond.await();
                }

                if (counter < limit) {
                    System.out.println("Thread :" + Thread.currentThread().threadId() + " counter : " + counter);
                    counter++;
                    oddCond.signal();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ThreadSolution solution = new ThreadSolution();
//        Thread t1 = new Thread(solution::printOddWithCond);
//        Thread t2 = new Thread(solution::printEvenWithCond);
//
//        t1.start();
//        t2.start();

        ;
        Thread t3 = new Thread(solution::printEvenWithLock);
        Thread t4 = new Thread(solution::printOddWithLock);

        t3.start();
        t4.start();
    }
}
