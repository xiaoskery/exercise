package com.study;

import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {

    public static void main(String[] args) {

        LockSupport.unpark(Thread.currentThread());

        LockSupport.park();

        System.out.println("hello");

        LockSupport.unpark(Thread.currentThread());

        LockSupport.unpark(Thread.currentThread());

        LockSupport.park();

        LockSupport.park();
        System.out.println("hello");

    }
}
