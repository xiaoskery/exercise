package com.study;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * reactor模型
 */
public class NIOServer3 {

    /**
     * 工作线程
     */
    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * reactor线程，mainReactor和subReactor的抽象
     */
    abstract class ReactorThread extends Thread {

        private volatile boolean running = false;
        private Selector selector;

        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

        public ReactorThread() throws Exception {
            selector = Selector.open();
        }

        public SelectionKey register(SelectableChannel channel) throws Exception {
            // 放在一个线程里执行，据说这样是为了更快
            FutureTask<SelectionKey> futureTask = new FutureTask<>(() -> channel.register(selector, 0, channel));
            queue.add(futureTask);
            return futureTask.get();
        }

        @Override
        public void run() {
            while (running) {
                Runnable task;
                try {
                    while ((task = queue.poll()) != null) {
                        task.run();
                    }
                    selector.select(1000);

                    // 获取事件
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> itr = selectionKeys.iterator();
                    while (itr.hasNext()) {
                        SelectionKey key = itr.next();
                        itr.remove();

                        int readyOps = key.readyOps();
                        if ((readyOps & (SelectionKey.OP_READ | SelectionKey.OP_ACCEPT)) != 0) {
                            SelectableChannel selectableChannel = (SelectableChannel)key.attachment();
                            selectableChannel.configureBlocking(false);

                            handle(key);

                            if (!selectableChannel.isOpen()) {
                                key.cancel();
                            }
                        }
                    }

                    selector.selectNow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        abstract void handle(SelectionKey key) throws Exception;

        public void doStart() {
            if (!running) {
                running = true;
                start();
            }
        }
    }

    private ServerSocketChannel serverSocketChannel;

    /**
     * accept线程组
     */
    private ReactorThread[] mainReactors = new ReactorThread[1];
    /**
     * io 线程组
     */
    private ReactorThread[] subReactors = new ReactorThread[8];

    // 初始化reactor线程
    private void initReactors() throws Exception {

        for (int i = 0; i < mainReactors.length; i++) {
            mainReactors[i] = new ReactorThread() {
                AtomicInteger inc = new AtomicInteger(0);

                @Override
                void handle(SelectionKey key) throws Exception {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.attachment();
                    SocketChannel ch = serverSocketChannel.accept();
                    ch.configureBlocking(false);

                    int index = inc.getAndIncrement() % subReactors.length;
                    subReactors[index].doStart();
                    SelectionKey selectionKey = subReactors[index].register(ch);
                    selectionKey.interestOps(SelectionKey.OP_READ);

                    System.out.println(Thread.currentThread().getName() + ",收到新连接：" + ch.getRemoteAddress());
                }
            };
        }

        for (int i = 0; i < subReactors.length; i++) {
            subReactors[i] = new ReactorThread() {

                @Override
                void handle(SelectionKey key) throws Exception {
                    SocketChannel socketChannel = (SocketChannel)key.attachment();

                    try {
                        // 申请buffer读取数据
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                        while (socketChannel.isOpen() && socketChannel.read(readBuffer) != -1) {
                            // 读到数据就结束
                            if (readBuffer.position() > 0)
                                break;
                        }

                        if (readBuffer.position() == 0) {
                            return;
                        }

                        // 读取数据
                        readBuffer.flip();
                        byte[] content = new byte[readBuffer.limit()];
                        readBuffer.get(content);
                        String s = new String(content, "utf-8");
                        System.out.println(Thread.currentThread().getName() + ",收到数据：" + s);

                        threadPool.execute(() -> {
                            System.out.println(Thread.currentThread().getName() + ",处理数据：" + s);
                        });

                        // 响应http请求
                        String msg = "HTTP/1.1 200 OK\r\n" + "Content-Length:11\r\n\n" + "Hello World";
                        ByteBuffer rsp = ByteBuffer.wrap(msg.getBytes());
                        while (rsp.hasRemaining()) {
                            socketChannel.write(rsp);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                }
            };
        }
    }

    // 创建serverSocketChannel并注册到mainReactor
    private void register() throws Exception {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        int index = new Random().nextInt(mainReactors.length);
        mainReactors[index].doStart();
        SelectionKey selectionKey = mainReactors[index].register(serverSocketChannel);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
    }

    private void bind() throws Exception {
        serverSocketChannel.bind(new InetSocketAddress(80));
        System.out.println("服务启动成功");
    }

    public static void main(String[] args) throws Exception {
        NIOServer3 nioServer3 = new NIOServer3();
        nioServer3.initReactors();
        nioServer3.register();
        nioServer3.bind();
    }
}
