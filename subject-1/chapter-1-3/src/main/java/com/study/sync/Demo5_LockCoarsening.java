package com.study.sync;

// 锁粗化(运行时 jit 编译优化)
// jit 编译后的汇编内容, jitwatch可视化工具进行查看
public class Demo5_LockCoarsening {



    public void test1(Object arg) {

        int i = 0;

        synchronized (this){
            i++;
        }
        synchronized (this){
            i--;
        }
        System.out.println("fsfdsdf....");

        synchronized (this){
            System.out.println("dfasdfad");
        }
        synchronized (this){
            i++;
        }


        synchronized (this){
            i++;
            i--;
            System.out.println("fsfdsdf....");
            System.out.println("dfasdfad");
            i++;
        }



    }

}
