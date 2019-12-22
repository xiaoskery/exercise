package com.study;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer2 {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0, serverSocketChannel);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);

        serverSocketChannel.socket().bind(new InetSocketAddress(80));
        System.out.println("服务启动成功");

        while (true) {
            selector.select();

            // 获取事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> itr = selectionKeys.iterator();
            while (itr.hasNext()) {
                SelectionKey key = itr.next();
                itr.remove();

                // 这里只关注accept和read事件
                if (key.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel)key.attachment();
                    SocketChannel socketChannel = ssc.accept();
                    socketChannel.configureBlocking(false);

                    System.out.println("收到新连接：" + socketChannel.getRemoteAddress());
                    socketChannel.register(selector, SelectionKey.OP_READ, socketChannel);
                } else if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel)key.attachment();
                    try {
                        // 申请buffer读取数据
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                        while (socketChannel.isOpen() && socketChannel.read(readBuffer) != -1) {
                            // 读到数据就结束
                            if (readBuffer.position() > 0)
                                break;
                        }

                        if (readBuffer.position() == 0)
                            continue;

                        // 读取数据
                        readBuffer.flip();
                        byte[] content = new byte[readBuffer.limit()];
                        readBuffer.get(content);
                        System.out.println("收到数据：" + new String(content, "utf-8"));

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
            }

            // 让之前取消的事件，彻底失效
            selector.selectNow();
        }
    }

}
