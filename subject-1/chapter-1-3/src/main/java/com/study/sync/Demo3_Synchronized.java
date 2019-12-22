package com.study.sync;

/*
1、基本使用 用于实例方法、用于静态方法 隐式指定锁对象
2、用于代码块时，显示指定锁对象
3、锁的作用域
4、引申，如果是多个进程，怎么办？
 */

public class Demo3_Synchronized {
    public static void main(String args[]){
        Counter ct1 = new Counter();
        Counter ct2 = new Counter();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Counter.staticUpdate();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Counter.staticUpdate();
            }
        }).start();
    }

}


class Counter{

    private static int i = 0;

    // synchronized (this){}
    public synchronized void update() {
        //访问数据库
    }

    public void updateBlock(){
        synchronized (this){
            //访问数据库
        }
    }

    //synchronized (Counter.class)
    public static synchronized void staticUpdate(){
        //访问数据库
    }

    public static void staticUpdateBlock(){
        synchronized (Counter.class){
            //访问数据库
        }
    }

}