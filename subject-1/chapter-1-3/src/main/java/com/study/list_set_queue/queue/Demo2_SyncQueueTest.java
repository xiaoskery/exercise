package com.study.list_set_queue.queue;

import java.util.concurrent.SynchronousQueue;

/*
1、take会阻塞，直到取到元素
2、put时会阻塞，直到被get


3、若没有take方法阻塞等待，offer的元素可能会丢失
4、poll取不到元素，就返回null,如果正好有put被阻塞，可以取到
5、peek 永远只能取到null，不能让take结束阻塞

 */

public class Demo2_SyncQueueTest {
    static SynchronousQueue<String> syncQueue = new SynchronousQueue<>();

    //put时会阻塞，直到被get
    public static void test01() throws InterruptedException {
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000L);
                    System.out.println(syncQueue.poll());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        System.out.println("begain to put...");
        syncQueue.put("put_element");
        System.out.println("put done...");
    }

    //3、若没有take方法阻塞等待，offer的元素可能会丢失
    public static void test02() throws InterruptedException {
        syncQueue.offer("offered_element");

        System.out.println(syncQueue.poll());
    }

    //4、poll取不到元素，就返回null,如果正好有put被阻塞，可以取到
    public static void test03() throws InterruptedException {
/*        new Thread(){
            @Override
            public void run() {
                try {
                    syncQueue.put("put_element");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/

        Thread.sleep(200L);
        Object obj =  syncQueue.poll();
        System.out.println(obj);
    }

    //peek 永远只能取到null，不能让take结束阻塞
    public static void test04() throws InterruptedException {
        new Thread(){
            @Override
            public void run() {
                try {
                    syncQueue.put("put_element");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        Thread.sleep(200L);
        Object obj =  syncQueue.peek();
        System.out.println(obj);
    }


    public static void main(String args[]) throws InterruptedException {
        test02();
    }
}
