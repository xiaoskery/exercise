package com.study.sync;



import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class JamesLock implements Lock {
    //有一个变量代表谁或得了锁
    //private Thread owner = null;
    private AtomicReference<Thread> owner = new AtomicReference<>();

    private BlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();


    @Override
    public boolean tryLock() {
        return owner.compareAndSet(null, Thread.currentThread());
    }

    @Override
    public void lock() {
        if (!tryLock()){
            Thread curTh = Thread.currentThread();
            waiters.offer(curTh);
            for (;;){
                Thread head = waiters.peek();
                if (head == Thread.currentThread()){
                    if (!tryLock()){
                        LockSupport.park();
                    }else{
                        return;
                    }
                }else{
                    LockSupport.park();
                }
            }
        }
    }

    @Override
    public void unlock() {
        if (owner.compareAndSet(Thread.currentThread(), null)){
            LockSupport.unpark(waiters.poll());
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }



    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }



    @Override
    public Condition newCondition() {
        return null;
    }



}
