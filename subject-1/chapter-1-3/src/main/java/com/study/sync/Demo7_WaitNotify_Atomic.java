package com.study.sync;

public class Demo7_WaitNotify_Atomic {
    static volatile int i = 0;
    static volatile int j = 0;

    static String lc = "";

    public static void main(String args[]) throws InterruptedException {
        Thread th = new Thread(){
            @Override
            public void run() {
                synchronized (lc){
                    i++;

                    try {
                        lc.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    j++;
                }
                System.out.println(i);
                System.out.println(j);

            }
        };
        th.start();

        Thread.sleep(1000);
        synchronized (lc){
            j++;
            lc.notifyAll();
        }



    }
}
