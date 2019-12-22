package com.study;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue {

    ReentrantLock lock = new ReentrantLock();

    Condition putCondition = lock.newCondition();
    Condition takeCondition = lock.newCondition();

    int size;
    List<Object> list = new ArrayList<>();

    public MyBlockingQueue(int size) {
        this.size = size;
    }

    public void put(Object obj) {
        lock.lock();
        try {
            while (true) {
                if (list.size() < size) {
                    System.out.println("添加的对象是：" + obj.toString());
                    list.add(obj);
                    takeCondition.signal();
                    break;
                } else {
                    System.out.println("满了，先等等");
                    putCondition.await();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public Object take() {
        lock.lock();
        try {
            while (true) {
                if (list.size() > 0) {
                    Object obj = list.remove(0);
                    System.out.println("拿到的对象是：" + obj.toString());
                    putCondition.signal();
                    return obj;
                } else {
                    System.out.println("没有对象，先等等");
                    takeCondition.await();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        MyBlockingQueue myBlockingQueue = new MyBlockingQueue(10);

        Thread thPut = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myBlockingQueue.put(new Object());
            }
        });
        thPut.start();

        Thread thTake = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myBlockingQueue.take();
            }
        });
        thTake.start();

    }
}
