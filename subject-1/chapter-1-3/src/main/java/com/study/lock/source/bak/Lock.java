package com.study.lock.source.bak;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;


public interface Lock {

    /** 若或得锁，立即返回
     *  若锁被占用，当前线程被挂起，直到获得锁
     */
    void lock();

    /**
     * 若获取锁，立即返回
     * 若锁被占用，线程挂起，知道出现如下状况之一：
     * 1、锁被当前线程获取
     * 2、方法被调用后，这个线程的状态被设置成interrupted，或调用了interupt()方法
     *
     * 注：在某些实现（impl）中无法做到对 等待锁 这个动作的中断，及时做到了，也会是一个昂贵(expensive)的操作
     * 用这个方法时需要注意，最好看一看impl的文档对该方法的描述
     */
    void lockInterruptibly() throws InterruptedException;

    /** 尝试获得锁，若锁被占用，立即退出，不阻塞
     */
    boolean tryLock();

    /**
     *尝试或得锁，并等待指定时长的锁
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;


    void unlock();


    Condition newCondition();
}

