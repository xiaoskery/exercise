package com.study.list_set_queue.queue;

import java.util.concurrent.PriorityBlockingQueue;

public class Demo3_PriorityBlockingQueue {
    public static void main(String args[]){
        PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<>(3);

        queue.put("48");
        queue.put("01");
        queue.put("12");
        queue.put("27");

        queue.put("31");


        for (;queue.size()>0;){
            try {
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}




