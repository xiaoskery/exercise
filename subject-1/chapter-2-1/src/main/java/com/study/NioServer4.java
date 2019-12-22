package com.study;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Reactor线程模型实现 实现注意点 1、主线程负责accept 2、子线程负责read 3、工作线程负责处理业务 4、为了更快的注册事件到selector，注册的动作和执行的动作在一个线程会更快
 * 5、主子线程共同的select相关的操作抽象成父类
 *
 */
public class NioServer4 {
    // serverSocketChannel
    private ServerSocketChannel serverSocketChannel;

    // 主线程组
    private ReactorThread[] mainReactors = new ReactorThread[1];

    // 子线程组
    private ReactorThread[] subReactors = new ReactorThread[8];

    // 业务线程池
    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    // reactor线程抽象父类
    abstract class ReactorThread extends Thread {
        private Selector selector;

        private LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue();

        private volatile boolean running = false;

        public ReactorThread() throws IOException {
            selector = Selector.open();
        }

        public void run() {

            while (running) {
                Runnable task;
                while ((task = taskQueue.poll()) != null) {
                    task.run();
                }

                try {
                    selector.select(1000);

                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        int readyOps = key.readyOps();

                        // 假设这里我们只关注accept和read事件
                        SelectableChannel ch = (SelectableChannel)key.attachment();
                        if ((readyOps & (SelectionKey.OP_ACCEPT | SelectionKey.OP_READ)) != 0) {
                            handle(key);
                        }

                        if (!ch.isOpen()) {
                            key.cancel();
                        }
                    }

                    selector.selectNow();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public abstract void handle(SelectionKey key);

        public SelectionKey register(SelectableChannel channel)
            throws ExecutionException, InterruptedException, IOException {
            channel.configureBlocking(false);

            // 这里采用异步注册，目的是为了让注册和mainReactor的执行线程在同一个线程，这样更快
            FutureTask<SelectionKey> task = new FutureTask(() -> channel.register(selector, 0, channel));
            taskQueue.add(task);
            return task.get();
        }

        public void doStart() {
            if (!running) {
                running = true;

                start();
            }
        }
    }

    // 初始化reactor线程组
    public void initReactors() throws IOException {
        // 初始化mainReactors
        for (int i = 0; i < mainReactors.length; i++) {
            mainReactors[i] = new ReactorThread() {
                // 轮询用计数
                AtomicInteger inc = new AtomicInteger(0);

                @Override
                public void handle(SelectionKey key) {
                    ServerSocketChannel ssc = (ServerSocketChannel)key.attachment();
                    try {
                        // 获取到新连接
                        SocketChannel ch = ssc.accept();

                        // 轮询选取subReactor线程，并启动它
                        int index = inc.getAndIncrement() % subReactors.length;
                        ReactorThread subReactor = subReactors[index];
                        subReactor.doStart();

                        // 注册读事件
                        SelectionKey selectionKey = subReactor.register(ch);
                        selectionKey.interestOps(SelectionKey.OP_READ);

                        System.out.println(Thread.currentThread().getName() + ", 收到新连接来自：" + ch.getRemoteAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        // 初始化subReactors
        for (int i = 0; i < subReactors.length; i++) {
            subReactors[i] = new ReactorThread() {
                @Override
                public void handle(SelectionKey key) {
                    SocketChannel ch = (SocketChannel)key.attachment();
                    try {
                        // 申请buffer读取数据
                        ByteBuffer reqBuffer = ByteBuffer.allocate(1024);

                        // 没有数据
                        if (ch.read(reqBuffer) == 0) {
                            return;
                        }

                        while (ch.isOpen() && ch.read(reqBuffer) != -1) {
                            if (reqBuffer.position() > 0) {
                                break;
                            }
                        }

                        // 没有数据
                        if (reqBuffer.position() == 0) {
                            return;
                        }

                        // 开始读取数据
                        reqBuffer.flip();
                        byte[] content = new byte[reqBuffer.limit()];
                        reqBuffer.get(content);
                        String contentStr = new String(content, "utf-8");
                        System.out.println(
                            Thread.currentThread().getName() + ",收到数据：" + contentStr + ", 来自：" + ch.getRemoteAddress());

                        threadPool.execute(() -> {
                            try {
                                System.out.println(Thread.currentThread().getName() + ",业务处理完成：" + contentStr + ", 来自："
                                    + ch.getRemoteAddress());;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        // 响应http请求
                        String rsp = "HTTP/1.1 200 OK\r\n" + "Content-Length:11\r\n\r\n" + "Hello World";
                        ByteBuffer rspBuffer = ByteBuffer.wrap(rsp.getBytes());
                        while (rspBuffer.hasRemaining()) {
                            ch.write(rspBuffer);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
    }

    // 初始化ServerSocketChannel并注册到mainReactor
    public void initAndRegister() throws IOException, ExecutionException, InterruptedException {
        // 1、创建
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        // 2、从mainReactor中选取Reactor线程并启动该线程
        int index = new Random().nextInt(mainReactors.length);
        mainReactors[index].doStart();

        // 3、注册accept事件
        SelectionKey selectionKey = mainReactors[index].register(serverSocketChannel);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
    }

    // 绑定端口启动监听
    public void bind(int port) throws IOException {
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        System.out.println("服务启动成功，正在监听端口：" + port);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        NioServer4 nioServer4 = new NioServer4();
        nioServer4.initReactors();
        nioServer4.initAndRegister();
        nioServer4.bind(80);
    }

}
