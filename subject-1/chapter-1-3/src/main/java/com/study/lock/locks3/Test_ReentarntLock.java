package com.study.lock.locks3;


public class Test_ReentarntLock {
    volatile static int i=0;

    static JamesReentrantLock lc = new JamesReentrantLock();

    public static void add(){
        lc.lock();
        i++;
        lc.unLock();
    }

    public static void main(String args[]) throws InterruptedException {
        for (int i=0; i<10; i++){
            new Thread(){
                @Override
                public void run() {
                    for (int j=0; j< 10000; j++){
                        //System.out.println(currentThread().getName()+ "....");
                        add();
                    }
                    System.out.println("done...");
                }
            }.start();
        }

        Thread.sleep(6000L);
        System.out.println(i);

    }
}
