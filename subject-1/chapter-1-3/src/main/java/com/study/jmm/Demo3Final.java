package com.study.jmm;

public class Demo3Final {
    final int x;
    int y;

    static Demo2Final f;

    public Demo3Final(){
        x = 3;
        //#### 重点 语句 #####
        y = x;      //因为x被final修饰了，所以可读到y的正确构造版本
    }

    static void writer(){
        f = new Demo2Final();
    }

    static void reader(){
        if (f!=null){
            int i = f.x;        //一定读到正确构造版本
            int j = f.y;        //也能读到正确构造版本
            System.out.println("i=" + i + ", j=" +j);
        }
    }


    public static void main(String args[]) throws InterruptedException {
        // Thread1 writer
        // Thread2 reader
    }
}
