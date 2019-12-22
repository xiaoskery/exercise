package com.study.cas;

public class Demo2_ImmutableObj {
    private int value = 0;

    public Demo2_ImmutableObj(int value){
        this.value = value;
    }

    //只有get方法
    public int getValue(){
        return this.value;
    }
}
