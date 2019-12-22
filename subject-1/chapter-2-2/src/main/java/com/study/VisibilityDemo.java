package com.study;

public class VisibilityDemo {
    int i = 0;
    public boolean isRunning = true;

    public static void main(String[] args) throws InterruptedException {
        VisibilityDemo demo = new VisibilityDemo();
        new Thread(() -> {
            System.out.println("here i am...");
            while (demo.isRunning) {
                demo.i++;
            }
            System.out.println(demo.i);
        }).start();

        Thread.sleep(3000L);
        demo.isRunning = false;
        System.out.println("shutdown...");
    }
}
