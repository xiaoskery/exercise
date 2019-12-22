package com.study.sync;

public class Demo1_SyncReEntry {


    public static synchronized void test1() throws InterruptedException {
        System.out.println(Thread.currentThread() + " 我开始执行 ");

        Thread.sleep(1000L);
        test1();

        System.out.println(Thread.currentThread() + " 我执行结束");
    }

    public static void main(String[] args) throws InterruptedException {
        test1();
    }



}
