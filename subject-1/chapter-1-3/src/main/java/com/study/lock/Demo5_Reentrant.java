package com.study.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class Demo5_Reentrant {

    static Lock lock =  new ReentrantLock();  //可重入锁

    public static void main(String args[]) throws InterruptedException {
        lock.lock();    //当前线程已获取锁
        System.out.println("get lock 1...");

        lock.lock();    //再次获取，是否能成功
        System.out.println("get lock 2...");

        lock.unlock();
        lock.unlock();

        //recersive();
    }



    public static void recersive(){
        lock.lock();

        System.out.println("here i am...");
        LockSupport.parkNanos(1000 * 1000 * 1000);
        recersive();

        lock.unlock();
    }
}
