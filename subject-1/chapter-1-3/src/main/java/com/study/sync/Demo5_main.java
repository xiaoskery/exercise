package com.study.sync;

public class Demo5_main {

    public static void main(String args[]){
        int a = 1;

        Teacher kody = new Teacher();
        kody.stu = new Student();


    }
}

class Teacher{
    String name = "Kody";
    int age = 40;
    boolean gender = true;

    Student stu;
}

class Student{
    String name = "Emily";
    int age = 18;
    boolean gender = false;
}
