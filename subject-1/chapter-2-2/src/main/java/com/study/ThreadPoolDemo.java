package com.study;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 责任链实现. 责任链四要素： 1、处理器抽象类 2、处理器实现类 3、处理器的保存 4、处理器的执行
 * 
 * @author Administrator
 */
public class ThreadPoolDemo {

    public static void main(String[] args) throws Exception, InterruptedException {

        ThreadPoolExecutor poolExecutor =
            new ThreadPoolExecutor(5, 10, 10l, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        for (int i = 0; i < 15; i++) {
            poolExecutor.submit(() -> {
                try {
                    // Thread.sleep(3000L);
                    System.out.println(Thread.currentThread().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName());
            });
        }

    }

}
