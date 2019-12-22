package com.study.concurrent_utils;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class JamesCountDownLatch {

    private Sync sync;

    public JamesCountDownLatch(int count){
        sync = new Sync(count);
    }

    public void countDown(){
        sync.releaseShared(1);
    }

    public void await(){
        sync.acquireShared(1);
    }




    class Sync extends AbstractQueuedSynchronizer{
        public Sync(int count){
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            //只有当state变为0时，加锁成功
            return getState()==0 ? 1: -1;
        }

        /*
                countdown的方法
                 */
        @Override
        protected boolean tryReleaseShared(int arg) {
            for (;;){
                int c = getState();
                if (c == 0)
                    return false;
                int nextc = c -1;
                //用CAS操作，讲count减一
                if (compareAndSetState(c , nextc)){
                    //当state=0时，释放锁成功，返回true
                    return nextc ==0;
                }
            }
        }
    }
}
