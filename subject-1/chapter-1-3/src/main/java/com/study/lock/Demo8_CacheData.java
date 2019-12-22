package com.study.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
// 缓存示例

 */

public class Demo8_CacheData {

    public static void main(String args[]){
        //System.out.println(TeacherInfoCache.get("Kody"));;
    }

}


class TeacherInfoCache {
    static volatile boolean cacheValid;
    static final ReadWriteLock rwl = new ReentrantReadWriteLock();

    static Object get(String dataKey){
        Object data = null;

        //读取数据，加读锁
        rwl.readLock().lock();
        try {
            if (cacheValid){
                data = Redis.data.get(dataKey);
            }else{
                //通过加锁的方式去访问DB，加写锁
                rwl.readLock().unlock();

                rwl.writeLock().lock();
                try {
                    if (!cacheValid){
                        data = DataBase.queryUserInfo();
                        Redis.data.put(dataKey, data);

                        cacheValid = true;
                    }
                }finally {
                    rwl.readLock().lock();
                    rwl.writeLock().unlock();
                }
            }
            return data;
        }finally {
            rwl.readLock().unlock();
        }
    }
}


class DataBase{
    static String queryUserInfo(){
        System.out.println("查询数据库。。。");
        return "name:Kody,age:40,gender:true,";
    }
}

class Redis{
    static Map<String, Object> data = new HashMap<>();
}



