package com.study;

/**
 * 责任链实现. 责任链四要素： 1、处理器抽象类 2、处理器实现类 3、处理器的保存 4、处理器的执行
 * 
 * @author Administrator
 */
public class PipelineDemo {
    private HandlerContext head;
    private HandlerContext tail;

    public PipelineDemo() {
        // 初始化头结点，这里很重要，它其实啥也不干，负责执行下一个
        head = new HandlerContext(new AbstractHandler() {
            @Override
            void handle(HandlerContext context, Object arg) {
                head.runNext(arg);
            }
        });
    }

    public static void main(String[] args) {
        PipelineDemo pipelineDemo = new PipelineDemo();
        pipelineDemo.addLast(new Handler1());
        pipelineDemo.addLast(new Handle2r());
        pipelineDemo.requestProcess("hello");

    }

    // 责任链开始执行，注意它的参数是事件
    public void requestProcess(Object arg) {
        this.head.handler(arg);
    }

    // 加到最后
    public void addLast(AbstractHandler handler) {
        HandlerContext context = head;
        while (context.next != null) {
            context = context.next;
        }
        context.next = new HandlerContext(handler);
    }

}

// 处理器抽象类
abstract class AbstractHandler {
    abstract void handle(HandlerContext context, Object arg);
}

// 处理器实现类1
class Handler1 extends AbstractHandler {

    @Override
    void handle(HandlerContext context, Object arg) {
        System.out.println(arg + "尾巴1111111.");

        // 通过上线文执行下一个
        context.runNext(arg);
    }
}

// 处理器实现类2
class Handle2r extends AbstractHandler {
    @Override
    void handle(HandlerContext context, Object arg) {
        System.out.println(arg + "尾巴2222222.");

        // 通过上线文执行下一个
        context.runNext(arg);
    }
}

// 处理器上下文，主要接口可以执行和执行下一个
class HandlerContext {
    AbstractHandler handler;
    HandlerContext next;

    public HandlerContext(AbstractHandler handler) {
        this.handler = handler;
    }

    public void handler(Object arg) {
        this.handler.handle(this, arg);
    }

    public void runNext(Object arg) {
        if (next != null) {
            next.handler(arg);
        }
    }
}
