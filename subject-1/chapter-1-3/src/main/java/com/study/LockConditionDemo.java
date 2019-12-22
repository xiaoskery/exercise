package com.study;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionDemo {

    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) throws Exception {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();

                try {
                    System.out.println("1111");
                    condition.await();
                    System.out.println("2222");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
            }
        });
        th.start();
        Thread.sleep(5000L);

        lock.lock();
        System.out.println("3333");
        condition.signal();
        lock.unlock();
    }
}
