package com.study;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyCyclicBarrier {

    public static void main(String[] args) throws InterruptedException {
        MyCyclicBarrier barrier = new MyCyclicBarrier(5);

        System.out.println("开启式抢锁");
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                barrier.await();
                System.out.println("我准备好了" + Thread.currentThread().getName());
                try {
                    Thread.sleep(2000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();
            Thread.sleep(2000l);
        }

        System.out.println("我抢到了");
    }

    private int parties;

    private int count;

    private Object generation = new Object();

    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public MyCyclicBarrier(int parties) {
        this.parties = parties;
    }

    public void await() {
        lock.lock();
        try {
            final Object currentGeneration = generation;

            count++;
            if (count == parties) {
                nextGeneration();
                return;
            }

            for (;;) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (currentGeneration != generation) {
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private void nextGeneration() {
        count = 0;
        condition.signalAll();
        generation = new Object();
    }
}
