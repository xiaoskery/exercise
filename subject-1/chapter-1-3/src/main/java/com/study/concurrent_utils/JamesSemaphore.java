package com.study.concurrent_utils;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class JamesSemaphore {

    private Sync sync;

    public JamesSemaphore(int permits){
        sync = new Sync(permits);
    }

    public void acquire(){
        sync.acquireShared(1);
    }

    public void release(){
        sync.releaseShared(1);
    }



    class Sync extends AbstractQueuedSynchronizer{
        private int permits;

        public Sync(int permits){
            this.permits = permits;
        }

        @Override
        protected int tryAcquireShared(int arg) {
            int state = getState();
            int nextState = state + arg;

            //如果信号量没占满，加锁的个数没有达到permits
            if (nextState <= permits){
                if (compareAndSetState(state, nextState))
                    return 1;
            }
            return -1;
        }


        @Override
        protected boolean tryReleaseShared(int arg) {
            int state = getState();
            if (compareAndSetState(state, state - arg)){
                return true;
            }else{
                return false;
            }
        }
    }
}
