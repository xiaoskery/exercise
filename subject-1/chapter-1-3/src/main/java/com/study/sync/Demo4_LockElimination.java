package com.study.sync;

//JIT即时编译时，进行了锁消除
public class Demo4_LockElimination {

    public void test1(Object arg) {

        //StringBuilder线程不安全，StringBuffer用了synchronized，是线程安全的
        // jit 优化, 消除了锁
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("a");
        stringBuffer.append("b");
        stringBuffer.append("c");

        stringBuffer.append("a");
        stringBuffer.append("b");
        stringBuffer.append("c");

        stringBuffer.append("a");
        stringBuffer.append("b");
        stringBuffer.append("c");
        // System.out.println(stringBuffer.toString());
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000000; i++) {
            new Demo4_LockElimination().test1("123");
        }
    }
}
