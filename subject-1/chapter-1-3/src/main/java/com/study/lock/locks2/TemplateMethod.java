package com.study.lock.locks2;

public class TemplateMethod {
    public static void main(String args[]){
        PPT01 ppt = new PPT01();
        ppt.show();

        System.out.println("\n\n\n\n");

        PPT02 ppt02 = new PPT02();
        ppt02.show();



    }
}

//PPT母版
class MotherMash{
    //请填入主题
    void title(){
        throw new UnsupportedOperationException();
    };

    //请给出正文内容
    void content(){
        throw new UnsupportedOperationException();
    }

    //请填补页脚
    void end(){
        throw new UnsupportedOperationException();
    }

    public final void show(){
        System.out.println("");
        title();
        System.out.println("    (标题=黑体、36号、绿色)");

        System.out.println("####正文开始  (华文行楷、28号、黑色)##########\n");
        content();
        System.out.println("\n####正文结束 ###############################");

        System.out.print("---------页脚：");
        end();
        System.out.println("---------------");
    }
}

class PPT01 extends MotherMash {

    @Override
    void title() {
        System.out.print("Lock接口的使用");
    }

    @Override
    void content() {
        System.out.println("ReentrantLock。。。");
        System.out.println("Conditiotn");
        System.out.println("wait noitfy");
    }

    @Override
    void end() {
        System.out.print("动脑学员版权所有");
    }
}


class PPT02 extends MotherMash {

    @Override
    void title() {
        System.out.print("AQS手写锁");
    }

    @Override
    void content() {
        System.out.println("ReadWriteLock搜狐写");
        System.out.println("ReeentrantLock的手写");
        System.out.println("wait noitfy");
    }

    @Override
    void end() {
        System.out.print("Kody....");
    }
}





