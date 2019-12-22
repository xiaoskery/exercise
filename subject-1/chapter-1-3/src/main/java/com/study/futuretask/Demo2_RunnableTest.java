package com.study.futuretask;

import java.util.concurrent.locks.LockSupport;

public class Demo2_RunnableTest {
    public static void main(String args[]){

        Runnable cmd = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        cmd.run();

        Thread th = new Thread(cmd);
        th.start();
        System.out.println("开启线程，实现异步，立即打印消息。。。");

    }


}

