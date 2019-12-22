package com.study.cas.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicTest {
    public static void main(String args[]){
        AtomicIntegerArray array = new AtomicIntegerArray(3);
        array.set(1, 14);
        array.compareAndSet(1,12,13);


        AtomicReference<Thread> th = new AtomicReference<>();
        th.get();
        th.compareAndSet(null,Thread.currentThread());



    }
}
