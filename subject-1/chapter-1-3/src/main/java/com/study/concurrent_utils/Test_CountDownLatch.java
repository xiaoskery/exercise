package com.study.concurrent_utils;

import java.util.concurrent.CountDownLatch;

public class Test_CountDownLatch {

    /*
    没隔1s开启一个线程，共开启6个线程
    若希望6个线程 同时 执行某一操作
    可以用CountDownLatch实现
     */
    public static void test01() throws InterruptedException {
        CountDownLatch ctl = new CountDownLatch(6);

        for (int i=0; i<6; i++){
            new Thread(){
                @Override
                public void run() {
                    ctl.countDown();
                    try {
                        ctl.await();
                        System.out.println("here I am...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            Thread.sleep(1000L);
        }
    }

    /*
    开启6个线程，main线程希望6个线程都执行完某个操作后，才执行某个操作
    可以用CountDownLatch来实现
     */
    public static void test02() throws InterruptedException {
        JamesCountDownLatch ctl = new JamesCountDownLatch(6);

        for (int i=0; i<6; i++){
            new Thread(){
                @Override
                public void run() {
                    System.out.println("after print...");
                    ctl.countDown();
                }
            }.start();
            Thread.sleep(1000L);
        }

        ctl.await();
        System.out.println("main thread do something ...");

    }



    public static void main(String args[]) throws InterruptedException {
        test02();
    }
}
