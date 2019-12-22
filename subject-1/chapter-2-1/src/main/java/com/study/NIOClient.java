package com.study;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NIOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(80));

        // 非阻塞，可能还没有连上，所以一直轮询
        while (!socketChannel.finishConnect()) {
            Thread.yield();
        }

        // 获取输入
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        String msg = scanner.nextLine();

        // 发送数据
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }

        // 读取服务端响应的数据
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        while (socketChannel.isOpen() && socketChannel.read(readBuffer) != -1) {
            if (readBuffer.position() > 0) {
                break;
            }
        }
        readBuffer.flip();
        byte[] content = new byte[readBuffer.limit()];
        readBuffer.get(content);
        System.out.println("收到响应：" + new String(content, "utf-8"));

        scanner.close();
        socketChannel.close();
    }
}
