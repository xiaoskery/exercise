package com.study.futuretask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.LockSupport;

public class Demo3_CallableTest {
    public static void main(String args[]) throws InterruptedException, ExecutionException {
        CallTask cTask = new CallTask();
        JamesFutureTask<String> fTask = new JamesFutureTask<String>(cTask);

        //执行第一次
        Thread th = new Thread(fTask);
        th.start();

        System.out.println("begain to get...");
        String result = fTask.get();
        System.out.println(result);

        //执行第二次
        Thread th1 = new Thread(fTask);
        th1.start();
    }
}

class CallTask implements Callable<String> {

    @Override
    public String call() throws Exception {
        LockSupport.parkNanos(1000 * 1000 *1000 * 5L);
        System.out.println("done...");
        return "James";
    }
}
