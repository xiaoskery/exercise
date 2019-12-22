package com.study.cas;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class CounterUnsafe {
    volatile int i = 0;

    private static Unsafe unsafe = null;

    //i字段的偏移量
    private static long valueOffset;

    static {
        //unsafe = Unsafe.getUnsafe();
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);

            Field fieldi = CounterUnsafe.class.getDeclaredField("i");
            valueOffset = unsafe.objectFieldOffset(fieldi);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void add() {
        //i++;
        for (;;){
            int current = unsafe.getIntVolatile(this, valueOffset);
            if (unsafe.compareAndSwapInt(this, valueOffset, current, current+1))
                break;
        }
    }
}
