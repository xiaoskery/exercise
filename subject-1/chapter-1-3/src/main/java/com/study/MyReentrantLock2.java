package com.study;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class MyReentrantLock2 implements Lock {

    // 锁的拥有者
    private AtomicReference<Thread> owner = new AtomicReference<>();
    // 锁的次数
    private AtomicInteger count = new AtomicInteger();
    // 等待队列
    private LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    static int num = 0;

    public static void main(String[] args) throws InterruptedException {

        Lock lock = new MyReentrantLock2();

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10000; j++) {
                        lock.lock();
                        try {
                            System.out.println(Thread.currentThread().getName() + " get lock.");
                            num++;
                            System.out.println(num);

                        } finally {
                            lock.unlock();
                            System.out.println(Thread.currentThread().getName() + " unlock.");
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void lock() {
        if (!tryLock()) {
            // 第一次抢锁失败，放入队列
            System.out.println(Thread.currentThread() + "入队");
            waiters.offer(Thread.currentThread());

            while (true) {
                Thread head = waiters.peek();
                if (head == Thread.currentThread()) {
                    // 我是队列的第一个，我可以去抢锁了，也可能强不成功
                    if (!tryLock()) {
                        // 抢失败，继续睡眠
                        LockSupport.park();
                    } else {
                        // 抢成功，出队
                        waiters.poll();
                        return;
                    }
                } else {
                    // 伪唤醒，继续睡眠
                    LockSupport.park();
                }
            }
        }
    }

    @Override
    public boolean tryLock() {
        int ct = count.get();
        if (ct == 0) {
            // 0表示锁没有被占用
            if (count.compareAndSet(ct, ct + 1)) {
                // 抢锁成功
                owner.set(Thread.currentThread());
                return true;
            } else {
                // 抢锁失败
                return false;
            }
        } else {
            // 看是否是自己拥有锁，是的话就是重入
            if (owner.get() == Thread.currentThread()) {
                // 是自己，可以重入
                count.set(ct + 1);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void unlock() {
        // 判断是否是当前线程，不是就抛异常
        if (tryUnlock()) {
            Thread head = waiters.peek();
            if (head != null) {
                LockSupport.unpark(head);
                System.out.println("unpark" + head.getName());
            }
        }
    }

    private boolean tryUnlock() {
        if (owner.get() != Thread.currentThread()) {
            throw new IllegalStateException();
        }

        int ct = count.get();
        int next = ct - 1;
        count.set(next);
        if (next == 0) {
            owner.compareAndSet(Thread.currentThread(), null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
