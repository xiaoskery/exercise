package com.study.cas;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterAtomic {
    //volatile int i = 0;
    AtomicInteger i = new AtomicInteger(0);

    public void add() {
        i.incrementAndGet();
    }
}
