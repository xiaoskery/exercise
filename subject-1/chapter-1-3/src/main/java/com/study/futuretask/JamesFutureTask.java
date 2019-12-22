package com.study.futuretask;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class JamesFutureTask<T> implements Runnable{

    //future只能执行一次
    private volatile int state = NEW;
    private static final int NEW =0;
    private static final int RUNNING = 1;
    private static final int FINISED = 2;


    public JamesFutureTask(Callable<T> task){
        this.callable = task;
    }

    //程序执行的结果
    private T result;

    //要自行的task
    Callable<T> callable;

    //获取结果的线层等待队列
    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>(100);

    //执行当前FutureTask的线程，用CAS进行争抢
    AtomicReference<Thread> runner = new AtomicReference<>();


    @Override
    public void run() {
        //判断当前对象的状态，如果是New就执行，如果
        if (state !=NEW ||
                !runner.compareAndSet(null,Thread.currentThread()))
            return;
        state = RUNNING;

        try {
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            state = FINISED;
        }

        //方法执行完，唤醒所有线程
        while (true){
            Thread waiter = waiters.poll();
            if (waiter == null)
                break;
            LockSupport.unpark(waiter);
        }

    }


    public T get(){
        if (state != FINISED){
            waiters.offer(Thread.currentThread());
        }

        while (state!=FINISED){
            LockSupport.park();
        }

        return result;
    }
}
