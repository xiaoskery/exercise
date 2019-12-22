package com.study;

import java.util.concurrent.locks.Lock;

public class LockDemo {

    // static Lock lock = new ReentrantLock();
    static Lock lock = new MyReentrantLock();

    public static void main(String[] args) throws Exception {
        lock.lock();

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("began to get lock");
                try {
                    lock.lockInterruptibly();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // lock.lock();
                /// lock.tryLock();
                System.out.println("succeed to get lock");
            }
        });
        th.start();
        Thread.sleep(7000L);

        th.interrupt();
    }
}
