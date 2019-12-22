package com.study.lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Demo6_ReadWrite02 {


    volatile long i = 0;

    ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void read() {
        rwLock.readLock().lock();

        long iValue = i;

        rwLock.readLock().unlock();
    }

    public void write() {
        rwLock.writeLock().lock();

        i++;

        rwLock.writeLock().unlock();
    }


    public static void main(String[] args) throws InterruptedException {
        final Demo6_ReadWrite02 demo = new Demo6_ReadWrite02();
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