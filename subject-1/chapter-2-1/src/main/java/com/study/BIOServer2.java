package com.study;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer2 {
    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("服务启动成功");

        while (!serverSocket.isClosed()) {
            Socket req = serverSocket.accept();

            threadPool.execute(() -> {
                System.out.println("收到新连接：" + req.toString());

                try {
                    // 读取并打印数据
                    InputStream inputStream = req.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    String msg;
                    // 读到没有为止
                    while ((msg = reader.readLine()) != null) {
                        if (msg.length() == 0) {
                            break;
                        }
                        System.out.println("收到数据：" + msg + ",来自：" + req.getRemoteSocketAddress());
                    }

                    // 响应HTTP 200数据
                    OutputStream out = req.getOutputStream();
                    out.write("HTTP/1.1 200 OK\r\n".getBytes());
                    out.write("Content-Length:11\r\n\r\n".getBytes());
                    out.write("Hello World".getBytes());
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        req.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
