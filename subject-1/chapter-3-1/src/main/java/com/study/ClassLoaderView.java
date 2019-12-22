package com.study;

public class ClassLoaderView {
    public static void main(String[] args) throws Exception {
        System.out.println(
            "核心类库加载器：" + ClassLoaderView.class.getClassLoader().loadClass("java.lang.String").getClassLoader());

        System.out.println("拓展类库加载器："
            + ClassLoaderView.class.getClassLoader().loadClass("com.sun.nio.zipfs.ZipCoder").getClassLoader());

        System.out.println("应用程序库加载器：" + ClassLoaderView.class.getClassLoader());

        System.out.println("应用程序库加载器的父类：" + ClassLoaderView.class.getClassLoader().getParent());

        System.out.println("应用程序库加载器的父类的父类：" + ClassLoaderView.class.getClassLoader().getParent().getParent());
    }

}
