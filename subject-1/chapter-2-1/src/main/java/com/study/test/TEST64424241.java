package com.study.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author wujiangming
 */
public class TEST64424241 {

    private static final String SERVER = "www.baidu.com";
    private static final String HEADER_NAME = "Server";

    public static void main(String[] args) throws IOException {
        // 创建连接对象
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        // 开始连接
        socketChannel.connect(new InetSocketAddress(SERVER, 80));

        // 等待连接完成
        while (!socketChannel.finishConnect()) {
            Thread.yield();
        }

        // 发送请求数据
        ByteBuffer writeBuffer = ByteBuffer.wrap(requestContent());
        while (writeBuffer.hasRemaining()) {
            socketChannel.write(writeBuffer);
        }

        // 循环读取数据，并拼接到StringBuilder中
        StringBuilder builder = new StringBuilder();
        int read = 0;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while ((read = socketChannel.read(buffer)) != -1) {
            if (read == 0) {
                continue;
            }

            // 先切换为读模式
            buffer.flip();

            // 将数据拼接到StringBuilder
            builder.append(new String(buffer.array()));

            // 清空buffer
            buffer.clear();
        }

        printHeaderValue(builder.toString(), HEADER_NAME);
    }

    /**
     * 打印给定请求头的值
     * 
     * @param content
     *            http响应内容
     * @param headerName
     *            响应头名称
     */
    private static void printHeaderValue(String content, String headerName) {
        if (content == null || content.length() == 0) {
            return;
        }

        // System.out.println(content);

        // 先将数据按\r\n分割，然后再按:分割
        String[] split = content.split("\\r\\n\\r\\n")[0].split("\\r\\n");
        if (split == null || split.length == 0) {
            return;
        }
        for (String s : split) {
            String[] kv = s.split(": ");
            if (kv == null || kv.length != 2) {
                continue;
            }
            if (kv[0].equalsIgnoreCase(headerName)) {
                System.out.println("我的QQ号：64424241，我的解析到百度服务器server类型是：" + s);
            }
        }
    }

    /**
     * 生成请求数据
     * 
     * @return Http请求byte数组
     */
    private static byte[] requestContent() {
        StringBuilder builder = new StringBuilder();
        builder.append("GET / HTTP/1.1\r\n");
        builder.append("Host: ").append(SERVER).append("\r\n");
        builder.append("Connection: close\r\n");
        builder.append("\r\n");
        return builder.toString().getBytes();
    }
}
