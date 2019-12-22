package com.study;

import java.util.concurrent.locks.Lock;

/**
 * 加锁次数要和解锁次数要一致 如果少于就不会释放，多余就会抛异常
 */
public class ReentrantLockDemo {

    // static Lock lock = new ReentrantLock();
    static Lock lock = new MyReentrantLock2();

    public static void main(String[] args) throws Exception {
        lock.lock();
        System.out.println("get lock 1");
        lock.unlock();

        lock.lock();
        System.out.println("get lock 2");
        lock.unlock();
        //lock.unlock();

        new Thread(() -> {
            System.out.println("start get lock");
            lock.lock();
            System.out.println("get lock");
            lock.unlock();
        }).start();
    }
}
