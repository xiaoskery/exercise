package com.study;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class MyReentrantLock implements Lock {
    // 锁的拥有者
    AtomicReference<Thread> owner = new AtomicReference<>();
    // 等待队列
    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();
    // 重入次数
    AtomicInteger count = new AtomicInteger();

    @Override
    public void lock() {
        if (!tryLock()) {
            // 入队列
            waiters.offer(Thread.currentThread());

            for (;;) {
                Thread head = waiters.peek();
                // 判断是否是头，防止伪唤醒
                if (head == Thread.currentThread()) {
                    if (!tryLock()) {
                        // 没抢到，继续park
                        LockSupport.park();
                    } else {
                        // 抢到锁出队列
                        waiters.poll();
                        // 退出死循环
                        return;
                    }
                } else {
                    // 伪唤醒，继续park
                    LockSupport.park();
                }
            }
        }
    }

    @Override
    public boolean tryLock() {
        // 判断count是否0\
        int ct = count.get();
        if (ct != 0) {
            // 锁被占用,是否是当前线程
            if (owner.get() == Thread.currentThread()) {
                count.set(ct + 1);
                return true;
            } else {
                return false;
            }
        } else {
            if (count.compareAndSet(0, 1)) {
                owner.set(Thread.currentThread());
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void unlock() {
        if (tryUnlock()) {
            Thread head = waiters.peek();
            if (head != null) {
                LockSupport.unpark(head);
            }
        }
    }

    public boolean tryUnlock() {
        // 判断是否是当前线程，不是就抛异常
        if (owner.get() != Thread.currentThread()) {
            throw new IllegalMonitorStateException();
        } else {
            // 加锁次数-1
            int ct = count.get();
            int next = ct - 1;
            count.set(next);

            // 若为0，解锁成功
            if (next == 0) {
                owner.compareAndSet(Thread.currentThread(), null);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
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
