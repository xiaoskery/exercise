package com.study.lock.locks5;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class JamesReadWriteLock implements ReadWriteLock {
    JamesAQS mask = new JamesAQS(){
        //尝试获取独占锁
        public boolean tryLock(int acquires) {
            //如果read count ！=0 返回false
            if (readCount.get() !=0)
                return false;

            int wct = writeCount.get();     //拿到 独占锁 当前状态

            if (wct==0){
                if (writeCount.compareAndSet(wct, wct + acquires)){     //通过修改state来抢锁
                    owner.set(Thread.currentThread());  //  抢到锁后，直接修改owner为当前线程
                    return true;
                }
            }else if (owner.get() == Thread.currentThread()){
                writeCount.set(wct + acquires);     //修改count值
                return true;
            }

            return false;
        }

        //尝试释放独占锁
        public boolean tryUnlock(int releases) {
            //若当前线程没有 持有独占锁
            if(owner.get()!= Thread.currentThread()){
                throw new IllegalMonitorStateException();       //抛IllegalMonitorStateException
            }

            int wc= writeCount.get();
            int nextc = wc - releases;      //计算 独占锁剩余占用
            writeCount.set(nextc);      //不管是否完全释放，都更新count值

            if (nextc==0){  //是否完全释放
                owner.compareAndSet(Thread.currentThread(), null);
                return true;
            }else{
                return false;
            }

        }


        //尝试获取共享锁
        public int tryLockShared(int acquires) {
            for (;;){
                if (writeCount.get()!=0 &&
                        owner.get() != Thread.currentThread())
                    return -1;

                int rct = readCount.get();
                if (readCount.compareAndSet(rct, rct + acquires)){
                    return 1;
                }
            }
        }

        //尝试解锁共享锁
        public boolean tryUnLockShared(int releases) {
            for(;;){
                int rc = readCount.get();
                int nextc = rc - releases;
                if (readCount.compareAndSet(rc, nextc)){
                    return nextc==0;
                }
            }
        }

    };


    @Override
    public Lock readLock() {
        return new Lock() {
            @Override
            public void lock() {
                mask.lockShared();
            }

            @Override
            public void lockInterruptibly() throws InterruptedException {

            }

            @Override
            public boolean tryLock() {
                return mask.tryLockShared(1) == 1;
            }

            @Override
            public void unlock() {
                mask.unLockShared();
            }

            @Override
            public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
                return false;
            }



            @Override
            public Condition newCondition() {
                return null;
            }
        };
    }

    @Override
    public Lock writeLock() {
        return new Lock() {
            @Override
            public void lock() {
                mask.lock();
            }


            @Override
            public boolean tryLock() {
                return mask.tryLock(1);
            }


            @Override
            public void unlock() {
                mask.unlock();
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
        };
    }
}
