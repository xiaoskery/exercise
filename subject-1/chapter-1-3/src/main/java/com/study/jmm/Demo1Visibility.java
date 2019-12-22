package com.study.jmm;

public class Demo1Visibility {
    int i = 0;
    boolean isRunning = true;

    public static void main(String args[]) throws InterruptedException {
        Demo1Visibility demo = new Demo1Visibility();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("here i am...");
                while(demo.isRunning){
                    synchronized (this){
                        demo.i++;
                    }
                }
                System.out.println(demo.i);
            }
        }).start();

        Thread.sleep(3000L);
        demo.isRunning = false;
        System.out.println("shutdown...");
    }
}
