package com.study;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReadWriteLock;

public class MyReadWriteLock implements ReadWriteLock {
    // 锁的拥有者
    AtomicReference<Thread> owner = new AtomicReference<>();
    // 等待队列
    LinkedBlockingQueue<WaitNode> waiters = new LinkedBlockingQueue();
    // 读锁次数
    AtomicInteger readCount = new AtomicInteger();
    // 写锁次数
    AtomicInteger writeCount = new AtomicInteger();

    @Override
    public Lock readLock() {
        if (!tryReadLock()) {
            waiters.offer(new WaitNode(Thread.currentThread(), 0));

            for (;;) {
                WaitNode head = waiters.peek();
                if (head.thread == Thread.currentThread()) {
                    if (!tryReadLock()) {
                        LockSupport.park();
                    } else {
                        waiters.poll();
                    }
                } else {
                    LockSupport.park();
                }
            }
        }
        return null;
    }

    public boolean tryReadLock() {
        // 判断写锁是否被占用
        int wct = writeCount.get();
        if (wct == 0) {
            // 写锁没有被占用，尝试获取读锁
            int rct = readCount.get();
            if (readCount.compareAndSet(rct, rct + 1)) {
                return true;
            } else {
                return false;
            }
        } else {
            // 写锁被占用
            return false;
        }
    }

    @Override
    public Lock writeLock() {
        return null;
    }

    class WaitNode {
        Thread thread = null;
        int type;// 0表示读，1表示写

        public WaitNode(Thread thread, int type) {
            this.thread = thread;
            this.type = type;
        }
    }
}
