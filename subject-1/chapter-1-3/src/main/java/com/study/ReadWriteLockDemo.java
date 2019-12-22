package com.study;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        ReadWriteLockDemo lock = new ReadWriteLockDemo();

        new Thread(() -> {
            lock.lockShared();
            System.out.println("get read lock");
            try {
                Thread.sleep(3000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("release read lock");
            lock.unLockShared();
        }).start();

        new Thread(() -> {
            lock.lockShared();
            System.out.println("get read lock");
            try {
                Thread.sleep(3000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("release read lock");
            lock.unLockShared();
        }).start();

        new Thread(() -> {
            lock.lock();
            System.out.println("get write lock");
            try {
                Thread.sleep(3000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("release write lock");
            lock.unLock();
        }).start();

        new Thread(() -> {
            lock.lock();
            System.out.println("get write lock");
            try {
                Thread.sleep(3000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("release write lock");
            lock.unLock();
        }).start();
    }

    private AtomicInteger readCount = new AtomicInteger();
    private AtomicInteger writeCount = new AtomicInteger();
    private AtomicReference<Thread> owner = new AtomicReference<>();
    private LinkedBlockingQueue<WaitNode> waiters = new LinkedBlockingQueue();

    class WaitNode {
        Thread thread;
        int type;// 0 read,1 write

        public WaitNode(int type, Thread thread) {
            this.type = type;
            this.thread = thread;
        }
    }

    public void lock() {
        if (!tryLock()) {
            waiters.offer(new WaitNode(1, Thread.currentThread()));

            while (true) {
                WaitNode head = waiters.peek();
                if (head != null && head.thread == Thread.currentThread()) {
                    if (tryLock()) {
                        waiters.poll();
                        return;
                    } else {
                        LockSupport.park();
                    }
                } else {
                    LockSupport.park();
                }
            }
        }
    }

    public boolean tryLock() {
        int rct = readCount.get();
        if (rct != 0) {
            return false;
        }

        int wct = writeCount.get();
        if (wct == 0) {
            if (writeCount.compareAndSet(0, 1)) {
                owner.set(Thread.currentThread());
                return true;
            }
        } else if (owner.get() == Thread.currentThread()) {
            writeCount.set(wct + 1);
            return true;
        }
        return false;
    }

    public void unLock() {
        if (tryUnlock()) {
            WaitNode head = waiters.peek();
            if (head != null) {
                LockSupport.unpark(head.thread);
            }
        }
    }

    private boolean tryUnlock() {
        if (owner.get() != Thread.currentThread()) {
            throw new IllegalStateException();
        }

        int wct = writeCount.get();
        int next = wct - 1;
        writeCount.set(next);

        if (next == 0) {
            owner.compareAndSet(Thread.currentThread(), null);
            return true;
        } else {
            return false;
        }
    }

    public void lockShared() {
        if (!tryLockShared()) {
            waiters.offer(new WaitNode(0, Thread.currentThread()));

            while (true) {
                WaitNode head = waiters.peek();
                if (head != null && head.thread == Thread.currentThread()) {
                    if (tryLockShared()) {
                        waiters.poll();

                        WaitNode next = waiters.peek();
                        if (next != null && next.type == 0) {
                            LockSupport.unpark(next.thread);
                        }
                        return;
                    } else {
                        LockSupport.park();
                    }
                } else {
                    LockSupport.park();
                }
            }
        }
    }

    private boolean tryLockShared() {
        while (true) {
            int wct = writeCount.get();
            if (wct != 0 && owner.get() != Thread.currentThread()) {
                return false;
            }

            int rct = readCount.get();
            if (readCount.compareAndSet(rct, rct + 1)) {
                return true;
            }
        }
    }

    public void unLockShared() {
        if (tryUnlockShared()) {
            WaitNode head = waiters.peek();
            if (head != null) {
                LockSupport.unpark(head.thread);
            }
        }
    }

    private boolean tryUnlockShared() {
        while (true) {
            int rct = readCount.get();
            int next = rct - 1;

            if (readCount.compareAndSet(rct, next)) {
                return readCount.get() == 0;
            }
        }
    }

}
