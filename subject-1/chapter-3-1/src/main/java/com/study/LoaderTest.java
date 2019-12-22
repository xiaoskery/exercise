package com.study;

import java.net.URL;
import java.net.URLClassLoader;

public class LoaderTest {
    public static void main(String[] args) throws Exception {
        URL classUrl = new URL("file:D:\\");
        URLClassLoader loader = new URLClassLoader(new URL[] {classUrl});
        while (true) {
            if (loader == null)
                break;

            Class clazz = loader.loadClass("HelloWorld");
            System.out.println("HelloWorld的类加载器：" + clazz.getClassLoader());

            Object obj = clazz.newInstance();
            Object v = clazz.getMethod("sayHello").invoke(obj);

            Thread.sleep(1000l);
            System.out.println();

            obj = null;
            loader = null;

        }

        System.gc();
        Thread.sleep(10000l);
    }
}
