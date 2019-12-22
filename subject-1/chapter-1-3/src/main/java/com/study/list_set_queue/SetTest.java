package com.study.list_set_queue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

public class SetTest {

    //Set中不会有相同的元素
    public static void test01(){
        System.out.println("test1===========================");
        Set<String> set = new HashSet<>();
        set.add("James");
        set.add("James");
        System.out.println(set.size());
    }

    //Set的语义是不保证数据顺序的
    public static void test02(){
        System.out.println("test2===========================");
        Set<Integer> nums = new HashSet<>();
        for (int i=1; i< 100000; i++){      //将i设置为10000
            nums.add(i);
        }

        for (int i : nums){
            System.out.println(i);
        }
    }

    //List的语义是保证数据顺序的
    public static void test03(){
        //List可以保证顺序
        List<Integer> list = new ArrayList<>(100000);
        for (int i=1; i< 100000; i++){      //将i设置为100000
            list.add(i);
        }

        for (int i : list){
            System.out.println(i);
        }

        list.get(0);
    }





    public static void main(String args[]){
        //test01();

        test02();


        System.out.println("test1===========================");
        CopyOnWriteArraySet<String> set2 = new CopyOnWriteArraySet<>();
        set2.add("James");
        set2.add("James");
        System.out.println(set2.size());

        System.out.println("test1===========================");
        ConcurrentSkipListSet<String> set3 = new ConcurrentSkipListSet<>();
        set2.add("James");
        set2.add("James");
        System.out.println(set2.size());

    }
}
