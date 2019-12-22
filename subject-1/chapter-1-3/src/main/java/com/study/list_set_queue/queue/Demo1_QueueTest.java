package com.study.list_set_queue.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Demo1_QueueTest {

    public static void test01_ArrayBlockingQueue() throws InterruptedException {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(12);

        queue.offer("");        //添加到队尾，        不阻塞
        queue.put("");
        queue.poll();              //取出头部，并移除，  不阻塞
        queue.peek();              //取出头部，不移除，  不阻塞


        try {
            queue.put("");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            queue.take();       //获取队列头部，并移除   无元素时，阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test02_LinkedBlockingQueue(){
        /*
           有界队列、无界队列
         */
        LinkedBlockingQueue<String> linkedQueue = new LinkedBlockingQueue<String>();
        linkedQueue.offer("");
        linkedQueue.poll();

        try {
            linkedQueue.put("");
            linkedQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void test03_ConcurrentLinkedQueue(){
        //非阻塞的度列
        ConcurrentLinkedQueue<String> conQueue = new ConcurrentLinkedQueue<>();
        conQueue.offer("");
        conQueue.poll();

        //conQueue.put("");
        //conQueue.take();
    }



    public static void main(String args[]) throws InterruptedException {

    }
}
