package com.study.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo3_Condition {
    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println("condition.await()");
                try {
                    condition.await();
                    System.out.println("here i am...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        });

        th.start();


        Thread.sleep(2000L);
        lock.lock();

        condition.signalAll();

        lock.unlock();

    }

}
