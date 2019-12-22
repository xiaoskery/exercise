package com.study.concurrent_utils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.LockSupport;

public class Test_CyclicBarrier {
    public static void main(String args[]){

        JamesCyclicBarrier barrier  = new JamesCyclicBarrier(4);

        //传入一个Runnable，打印栅栏

        for (int i=0; i< 100; i++){
            new Thread(){
                @Override
                public void run() {
                    barrier.await();    //
                    System.out.println("上到摩天轮...");
                }
            }.start();
            LockSupport.parkNanos(1000 * 1000 * 1000L);
        }
    }
}
