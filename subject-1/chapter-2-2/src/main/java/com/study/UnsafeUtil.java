package com.study;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

import sun.misc.Unsafe;

/**
 * 责任链实现. 责任链四要素： 1、处理器抽象类 2、处理器实现类 3、处理器的保存 4、处理器的执行
 * 
 * @author Administrator
 */
public class UnsafeUtil {

    private static Unsafe unsafe;
    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe)field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }

    public static long getObjectFieldOffset(Object obj, String filed) throws NoSuchFieldException {
        Field field = obj.getClass().getDeclaredField(filed);
        long offset =  unsafe.objectFieldOffset(field);
        return offset;
    }

    public static void add(Object obj, String filed) throws NoSuchFieldException {
        // 根据对象引用以及 字段的偏移量获取对应的值
        while (true) {
            long offset = getObjectFieldOffset(obj, filed);
            int count = unsafe.getIntVolatile(obj, offset);
            boolean b = unsafe.compareAndSwapInt(obj, offset, count, count + 1);
            if (b) {
                break;
            }
        }
    }

    public static void main(String[] args) throws NoSuchFieldException, InterruptedException {

        int threadCount = 1000;
        CountDownLatch latch = new CountDownLatch(threadCount);
        Counter counter = new Counter();

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    try {
                        UnsafeUtil.add(counter, "index");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        System.out.println(counter.getIndex());
    }

    static class Counter {
        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        private int index;

    }
}
