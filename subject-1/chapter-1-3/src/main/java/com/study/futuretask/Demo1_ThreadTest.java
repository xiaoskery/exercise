package com.study.futuretask;


public class Demo1_ThreadTest {
    public static void main(String args[]){


        new Thread(){
            @Override
            public void run() {
                System.out.println("dfasdfad");

            }
        }.start();


    }
}

