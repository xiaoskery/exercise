package com.study.cas;

public class CounterSync {
    volatile int i = 0;

    public synchronized void add() {
        i++;
    }
}
