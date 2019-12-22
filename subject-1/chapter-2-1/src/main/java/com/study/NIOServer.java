package com.study;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIOServer {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        System.out.println("服务启动成功");

        while (true) {
            SocketChannel req = serverSocketChannel.accept();

            // 因为NIO是非阻塞的，所以可能为空，需要不断的轮询
            if (req != null) {
                try {
                    System.out.println("收到新连接：" + req.getRemoteAddress());
                    req.configureBlocking(false);

                    // 申请buffer准备读数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    // 将数据读到buffer中
                    while (req.isOpen() && req.read(buffer) != -1) {
                        if (buffer.position() > 0) {
                            break;
                        }
                    }

                    // 切换成都模式
                    buffer.flip();
                    // 申请byte数组，大小为读到的数据的大小
                    byte[] content = new byte[buffer.limit()];
                    buffer.get(content);
                    System.out.println("收到数据：" + new String(content, "utf-8") + ", 来自：" + req.getRemoteAddress());

                    // 响应http请求
                    String msg = "HTTP/1.1 200 OK\r\n" + "Content-Length:11\r\n\n" + "Hello World";
                    ByteBuffer rsp = ByteBuffer.wrap(msg.getBytes());
                    while (rsp.hasRemaining()) {
                        req.write(rsp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
