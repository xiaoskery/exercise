package com.study.cas;

public class Counter {
    volatile int i = 0;

    public void add() {
        i++;
    }
}
