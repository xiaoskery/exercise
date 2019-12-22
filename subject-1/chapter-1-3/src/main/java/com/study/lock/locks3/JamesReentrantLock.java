package com.study.lock.locks3;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class JamesReentrantLock {
    CommonMask mask = new CommonMask();

    public void lock(){
        mask.lock();
    }

    public boolean tryLock(int acquires){
        return mask.tryLock(acquires);
    }

    public void unLock(){
        mask.unlock();
    }

}
