package com.study.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
  既有读，又有写时，
  读也需要加锁
 */
public class Demo6_ReadWrite01 {

    volatile long i = 0;

    Lock lock = new ReentrantLock();
    //ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void read() {
        lock.lock();

        long a = i;

        lock.unlock();
    }

    public void write() {
        lock.lock();

        i++;

        lock.unlock();
    }


    public static void main(String[] args) throws InterruptedException {
        final Demo6_ReadWrite01 demo = new Demo6_ReadWrite01();
        long startTme = System.currentTimeMillis();

        for (int i=0;i<=30; i++){
            int n = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long starttime = System.currentTimeMillis();

                    for (int j=0; j<400000; j++){
                        if (n==0) demo.write();
                        else demo.read();
                    }
                }
            }).start();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("耗时："+ (endTime - startTme));
    }


}
