package com.study.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo1_ReentrantTest {

    private static  int i = 0;

    //private final static Lock lc = new ReentrantLock();     //可重入锁

    private final static Lock lc = new JamesLock();   //不可重入锁


    public static void recursive() throws InterruptedException {
        lc.lock();

        i++;

        System.out.println("here i am...");
        Thread.sleep(1000L);
        recursive();

        lc.unlock();
    }


    public static void main(String args[]) throws InterruptedException {

        lc.lock();
        System.out.println("加锁第一次。。。");
        lc.lock();
        System.out.println("加锁第二次。。。");

        lc.unlock();
        lc.unlock();



        //recursive();
    }





}
