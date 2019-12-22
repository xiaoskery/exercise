package com.study;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockMap {
    Map<String, Object> map = new HashMap();

    ReentrantReadWriteLock rwl = null;
    ReentrantReadWriteLock.ReadLock rl;
    ReentrantReadWriteLock.WriteLock wl;

    public ReadWriteLockMap(boolean isFair) {
        this.rwl = new ReentrantReadWriteLock(isFair);
        this.rl = rwl.readLock();
        this.wl = rwl.writeLock();
    }

    public void put(String key, Object obj) {
        wl.lock();
        try {
            map.put(key, obj);
        } finally {
            rl.unlock();
        }
    }

    public Object get(String key) {
        rl.lock();
        try {
            return map.get(key);
        } finally {
            rl.unlock();
        }
    }

    public String[] keys() {
        rl.lock();
        try {
            return map.keySet().toArray(new String[0]);
        } finally {
            rl.unlock();
        }
    }

    public void clear() {
        wl.lock();
        try {
            map.clear();
        } finally {
            wl.unlock();
        }
    }
}
