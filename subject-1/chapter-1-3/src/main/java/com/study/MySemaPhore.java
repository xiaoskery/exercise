package com.study;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MySemaPhore {

    public static void main(String[] args) {
        MySemaPhore  semaPhore = new MySemaPhore(5);

        System.out.println("开启式抢锁");
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                semaPhore.acquire();
                System.out.println("我准备好了" + Thread.currentThread().getName());
                try {
                    Thread.sleep(2000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaPhore.release();
                }

            }).start();
        }

        System.out.println("我抢到了");
    }

    private Sync sync;

    public MySemaPhore(int count) {
        sync = new Sync(count);
    }

    public void acquire() {
        sync.acquireShared(1);
    }

    public void release() {
        sync.releaseShared(1);
    }

    class Sync extends AbstractQueuedSynchronizer {

        private int permits;

        public Sync(int count) {
            this.permits = count;
        }

        @Override
        protected int tryAcquireShared(int arg) {
            int c = getState();
            int nextc = c + 1;

            if (nextc <= permits) {
                // 这里不需要自旋，因为在AcquireShared里面有了
                if (compareAndSetState(c, nextc)) {
                    return 1;
                }
            }
            return -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (;;) {
                int c = getState();
                int nextc = c - 1;
                if (compareAndSetState(c, nextc)) {
                    return true;
                }
            }
        }
    }
}
