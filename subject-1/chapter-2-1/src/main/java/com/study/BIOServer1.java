package com.study;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer1 {
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
                            // break;
                        }
                        System.out.println("收到数据：" + msg + ",来自：" + req.getRemoteSocketAddress());
                    }
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
