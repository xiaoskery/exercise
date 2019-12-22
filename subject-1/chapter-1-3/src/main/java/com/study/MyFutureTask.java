package com.study;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class MyFutureTask<T> implements Runnable {

    public static void main(String[] args) {

        MyFutureTask<String> task = new MyFutureTask<>(new Callable() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(5000l);
                return "hello";
            }
        });

        Thread th = new Thread(task);
        th.start();

        System.out.println(task.get());
    }

    private volatile int state = NEW;
    private AtomicReference<Thread> runner = new AtomicReference<>();
    private static final int NEW = 0;
    private static final int RUNNING = 1;
    private static final int FINISHED = 2;

    private Callable<T> callable;
    private T result;

    private LinkedBlockingQueue<Thread> queue = new LinkedBlockingQueue();

    public MyFutureTask(Callable callable) {
        this.callable = callable;
    }

    public void run() {
        if (state != NEW || !runner.compareAndSet(null, Thread.currentThread())) {
            return;
        }

        state = RUNNING;

        try {
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            state = FINISHED;
        }

        while (true) {
            Thread th = queue.poll();
            if (th != null) {
                LockSupport.unpark(th);
            } else {
                return;
            }
        }
    }

    public T get() {
        if (state != FINISHED) {
            queue.offer(Thread.currentThread());
        }

        while (true) {
            if (state == FINISHED) {
                return result;
            }
            LockSupport.park();
        }
    }
}
