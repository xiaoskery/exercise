package com.study.cas.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class Demo2_AtomicIntegerFieldUpdater {
    // 新建AtomicIntegerFieldUpdater对象，需要指明是哪个类中的哪个字段


    private static AtomicIntegerFieldUpdater<User> atom =
            AtomicIntegerFieldUpdater.newUpdater(User.class, "id");

    public static void main(String[] args) {

        User user = new User(100, 100,"Kody");

        atom.addAndGet(user, 50);
        System.out.println("addAndGet(user, 50)             调用后值变为：" + user);

    }
}

class User {
    volatile int id;
    volatile int age;

    private String name;

    public User(int id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }

    public String toString() {
        return "id：" + id + " " + "age：" + age;
    }
}

