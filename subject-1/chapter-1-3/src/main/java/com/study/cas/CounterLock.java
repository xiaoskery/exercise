package com.study.cas;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterLock {
    volatile int i = 0;

    Lock lock = new ReentrantLock();

    public void add() {
        lock.lock();

        i++;

        lock.unlock();
    }
}
