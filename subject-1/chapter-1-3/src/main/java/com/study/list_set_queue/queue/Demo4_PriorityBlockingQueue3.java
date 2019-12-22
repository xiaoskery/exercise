package com.study.list_set_queue.queue;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class Demo4_PriorityBlockingQueue3 {
    public static void main(String args[]){
        PriorityBlockingQueue<Student> queue = new PriorityBlockingQueue<>(5, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                int num1 = o1.age;
                int num2 = o2.age;

                if (num1 > num2)
                    return 1;
                else if (num1 == num2)
                    return 0;
                else
                    return -1;
            }
        });
        queue.put(new Student(10, "enmily"));
        queue.put(new Student(20, "Tony"));
        queue.put(new Student(5, "baby"));

        for (;queue.size() >0;){
            try {
                System.out.println(queue.take().name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Student{
    public int age;
    public String name;

    public Student(int age, String name){
        this.age = age;
        this.name = name;
    }
}
