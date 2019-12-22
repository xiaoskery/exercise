package com.study.lock;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class JamesLock implements Lock {

    //锁的拥有者
    AtomicReference<Thread> owner = new AtomicReference<>();

    //等待队列
    private LinkedBlockingQueue<Thread> waiter = new LinkedBlockingQueue<>();




    @Override
    public boolean tryLock() {
        return owner.compareAndSet(null, Thread.currentThread());
    }

    @Override
    public void lock() {
        if (!tryLock()){
            waiter.offer(Thread.currentThread());

            for(;;){
                Thread head = waiter.peek();
                if (head == Thread.currentThread()){
                    if(!tryLock()){
                        //挂起线程
                        LockSupport.park();
                    }else{
                        //抢锁成功，将线程出度列
                        waiter.poll();
                        return;
                    }
                }else{
                    //线程挂起
                    LockSupport.park();
                }
            }
        }
    }

    @Override
    public void unlock() {
        if (tryUnlock()){
            Thread th = waiter.peek();
            if (th !=null){
                LockSupport.unpark(th);
            }
        }
    }

    public boolean tryUnlock(){
        //首先判断当前线程是否站有锁
        if (owner.get() !=Thread.currentThread()){
            throw new IllegalMonitorStateException();
        }else{
            return owner.compareAndSet(Thread.currentThread(), null);
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
