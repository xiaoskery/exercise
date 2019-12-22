package com.study.sync;

/*
  调用完notify后，线程并不会马上从阻塞中退出，
  抢到锁后才会继续执行
 */

import java.util.concurrent.locks.LockSupport;

public class Demo6_WaitNotifyTest {

    private static final long SECOND = 1000 * 1000 * 1000L;

    public static void main(String args[]) throws InterruptedException {
        Thread th = new Thread(){
            @Override
            public void run() {
                synchronized (Demo6_WaitNotifyTest.class){
                    try {
                        System.out.println("线程1 进入等待。。。");
                        Demo6_WaitNotifyTest.class.wait();
                        System.out.println("主线程释放锁后，才拿到锁，结束等待");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        th.start();

        new Thread(){
            @Override
            public void run() {
                LockSupport.parkNanos(1000 * 1000 * 1000);
                synchronized (Demo6_WaitNotifyTest.class){
                    System.out.println("如果我先打印，那么notify的线程没有特殊逻辑。。。");
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                LockSupport.parkNanos(1000 * 1000 * 1000 * 2);
                synchronized (Demo6_WaitNotifyTest.class){
                    System.out.println("线程2 拿到锁。。。");
                    Demo6_WaitNotifyTest.class.notify();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        while (true){
            System.out.println("th.state:" + th.getState());
            Thread.sleep(200L);
        }

    }

}
