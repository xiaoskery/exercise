package com.study.concurrent_utils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class JamesCyclicBarrier {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    //记录当前这个批次有多少个
    private int count = 0;

    //记录批次的大小
    private final int parties;

    //分代
    private Object generation = new Object();

    public JamesCyclicBarrier(int parties){
        if (parties <=0)
            throw new IllegalArgumentException();
        this.parties = parties;
    }

    //进入下一个分代
    public void nextGeneration(){
        condition.signalAll();
        count = 0;
        generation = new Object();
    }

    public void await(){
        //实现排队，需要将线程放到等待队列
        //还需要将线程挂起
        //
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            //记录当前的generation，相当于记录当前批次的id
            final Object g = generation;

            int index = ++count;
            //批次已经达到parties，
            if (index == parties){
                //进入下一个批次
                nextGeneration();
                return;
            }

            //若未达到批次，就进入等待
            for (;;){
                try {
                    condition.await();
                } catch (InterruptedException e) {

                }
                if (g != generation){
                    return;
                }
            }

        }finally {
            lock.unlock();
        }

    }

}
