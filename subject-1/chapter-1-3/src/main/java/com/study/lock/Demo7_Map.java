package com.study.lock;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// 将hashmap 改造一个并发安全的
// 这是ReentrantReadWriteLock注释中给出的一个示例
public class Demo7_Map {
    private final Map<String, Object> m = new HashMap<>();

    private final  ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    public Object get(String key){
        r.lock();
        try {
            return m.get(key);
        }finally {
            r.unlock();
        }
    }


    public Object[] allkeys(){
        r.lock();
        try {
            return m.keySet().toArray();
        }finally {
            r.unlock();
        }
    }


    public Object put(String key, Object obj){
        w.lock();
        try {
            return m.put(key, obj);
        }finally {
            w.unlock();
        }
    }

    public void clear(){
        w.lock();
        try {
            m.clear();
        }finally {
            w.unlock();
        }
    }





}