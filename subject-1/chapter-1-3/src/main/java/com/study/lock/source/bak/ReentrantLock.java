package com.study.lock.source.bak;


import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


public class ReentrantLock implements Lock, java.io.Serializable {
    private static final long serialVersionUID = 7373984872572414699L;
    /** Synchronizer providing all implementation mechanics */
    private final Sync sync;


    abstract static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -5179523762034025860L;

        /**
         * Performs {@link Lock#lock}. The main reason for subclassing
         * is to allow fast path for nonfair version.
         */
        abstract void lock();

        /** 执行非公平的tryLock，tryAcquire在子类中实现，但他们的tryLock中都需要nonfairTryAcquire
         * Performs non-fair tryLock.  tryAcquire is implemented in
         * subclasses, but both need nonfair try for trylock method.
         */
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();     //拿到state值
            if (c == 0) {   //若state值为0，锁没有被占用
                if (compareAndSetState(0, acquires)) {    //利用CAS操作来修改state值，来抢锁  acquires 是啥？？？
                    setExclusiveOwnerThread(current);        //讲当前线程设置为独占锁owner
                    return true;                //抢到所，返回true
                }
            }
            else if (current == getExclusiveOwnerThread()) {    //否则，拿到独占锁onwer
                int nextc = c + acquires;
                if (nextc < 0) // overflow  //重入不能超过int.Max_VALUE
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);        //更改状态值，记录重入次数
                return true;        //重入后，返回true
            }
            return false;       //锁被占用，返回false
        }

        protected final boolean tryRelease(int releases) {
            int c = getState() - releases;      //计算 release后state的状态
            if (Thread.currentThread() != getExclusiveOwnerThread())    //不是当前线程 正在占有锁
                throw new IllegalMonitorStateException();               //抛异常
            boolean free = false;       //
            if (c == 0) {       //release后，state变为0，当前线程已经不再占有锁
                free = true;    //只有在当前线程不占用锁的情况下，才返回true
                setExclusiveOwnerThread(null);  //讲独占的owner设置为null
            }
            setState(c);        //设置新的状态
            return free;        //若当前线程不再占用锁，返回true，否则返回false
        }

        protected final boolean isHeldExclusively() {
            // While we must in general read state before owner,
            // we don't need to do so to check if current thread is owner
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        // Methods relayed from outer class

        final Thread getOwner() {
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }

        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }

        final boolean isLocked() {
            return getState() != 0;
        }

        /**
         * Reconstitutes the instance from a stream (that is, deserializes it).
         */
        private void readObject(java.io.ObjectInputStream s)
                throws java.io.IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }
    }

    //Sync object for non-fair locks
    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = 7316153563782823691L;

        /**
         * Performs lock.  Try immediate barge, backing up to normal
         * acquire on failure.
         */
        final void lock() {
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }

    //Sync object for fair locks
    static final class FairSync extends Sync {
        private static final long serialVersionUID = -3000897897090466540L;

        final void lock() {
            acquire(1);
        }

        /**
         * Fair version of tryAcquire.  Don't grant access unless
         * recursive call or no waiters or is first.
         */
        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {   //若锁未占用
                if (!hasQueuedPredecessors() &&     //判断是否有比当前线程等待更久的线程，若没有
                        compareAndSetState(0, acquires)) {  //就修改state，抢锁
                    setExclusiveOwnerThread(current);           //抢到锁，将当前线程设置为独占锁onwer
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }


    public ReentrantLock() {    //默认为非公平锁
        sync = new NonfairSync();
    }


    public ReentrantLock(boolean fair) {    //传入true，则为公平锁
        sync = fair ? new FairSync() : new NonfairSync();
    }


    public void lock() {
        sync.lock();        //看起来很眼熟
    }


    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    //tryLock调用的是nonfairTryAcquire，说明若调用tryLock方法，就不能保证公平性
    public boolean tryLock() {
        return sync.nonfairTryAcquire(1);
    }


    public boolean tryLock(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }


    public void unlock() {
        sync.release(1);    //看起来很眼熟
    }


    public Condition newCondition() {
        return sync.newCondition();
    }


    public int getHoldCount() {
        return sync.getHoldCount();
    }


    public boolean isHeldByCurrentThread() {
        return sync.isHeldExclusively();
    }


    public boolean isLocked() {
        return sync.isLocked();
    }


    public final boolean isFair() {
        return sync instanceof FairSync;
    }


    protected Thread getOwner() {
        return sync.getOwner();
    }


    public final boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }


    public final boolean hasQueuedThread(Thread thread) {
        return sync.isQueued(thread);
    }


    public final int getQueueLength() {
        return sync.getQueueLength();
    }


    protected Collection<Thread> getQueuedThreads() {
        return sync.getQueuedThreads();
    }


    public boolean hasWaiters(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.hasWaiters((AbstractQueuedSynchronizer.ConditionObject)condition);
    }


    public int getWaitQueueLength(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitQueueLength((AbstractQueuedSynchronizer.ConditionObject)condition);
    }


    protected Collection<Thread> getWaitingThreads(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitingThreads((AbstractQueuedSynchronizer.ConditionObject)condition);
    }


    public String toString() {
        Thread o = sync.getOwner();
        return super.toString() + ((o == null) ?
                "[Unlocked]" :
                "[Locked by thread " + o.getName() + "]");
    }
}

