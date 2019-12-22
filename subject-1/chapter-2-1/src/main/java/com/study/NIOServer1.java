package com.study;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NIOServer1 {
    private static List<SocketChannel> channels = new ArrayList();

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(80));
        System.out.println("服务启动成功");

        while (true) {
            SocketChannel req = serverSocketChannel.accept();

            if (req != null) {
                // 收到新连接加入的集合中
                System.out.println("收到新连接：" + req.getRemoteAddress());
                req.configureBlocking(false);
                channels.add(req);
            } else {
                // 没有新连接请求的时候，我们来处理集合中的channel的读写
                Iterator<SocketChannel> iterator = channels.iterator();
                while (iterator.hasNext()) {
                    SocketChannel socketChannel = iterator.next();

                    try {
                        // 申请buffer读取数据
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        if (socketChannel.read(readBuffer) == 0) {
                            // 没有数据，继续下一个channel
                            continue;
                        }

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
                    }
                }
            }
        }
    }

}
