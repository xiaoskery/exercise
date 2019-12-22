package com.study.lock.locks1;

public class Test_ReadWriteLock {

    static JamesReadWriteLock rwLock = new JamesReadWriteLock();
    //static ReadWriteLock rwLock = new ReentrantReadWriteLock();

    static volatile int i =0;

    static void add(){
        i++;
    }

    public static void main(String args[]) throws InterruptedException {
        long startTime = System.currentTimeMillis();


        for (int a=1; a<=20000; a++){
            final int n = a;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (n%5 ==0){
                        rwLock.lock();
                        add();
                        rwLock.unlock();
                    }else{
                        rwLock.lockShared();
                        System.out.println("i=" +i);
                        int a = i;
                        rwLock.unLockShared();
                    }
                }
            }).start();
        }


        while (true){
            System.out.println("目前耗时：" + (System.currentTimeMillis()-startTime) /1000 + "s");
            Thread.sleep(1000L);
            System.out.println("i=" + i);

        }
    }
}
