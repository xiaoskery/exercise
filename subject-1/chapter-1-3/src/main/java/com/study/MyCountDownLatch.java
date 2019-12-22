package com.study;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MyCountDownLatch {

    public static void main(String[] args) {
        MyCountDownLatch latch = new MyCountDownLatch(5);

        System.out.println("开启式抢锁");
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                System.out.println("我准备好了" + Thread.currentThread().getName());
                latch.countDown();
            }).start();
        }
        latch.await();

        System.out.println("我抢到了");
    }

    private Sync sync;

    public MyCountDownLatch(int count) {
        sync = new Sync(count);
    }

    public void await() {
        sync.acquireShared(1);
    }

    public void countDown() {
        sync.releaseShared(1);
    }

    class Sync extends AbstractQueuedSynchronizer {

        public Sync(int count) {
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            int c = getState();
            return c == 0 ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (;;) {
                int c = getState();
                if (c == 0)
                    return false;

                int nextc = c - 1;
                if (compareAndSetState(c, nextc)) {
                    return nextc == 0;
                }
            }
        }
    }
}
