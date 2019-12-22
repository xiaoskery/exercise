package com.study;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class BIOClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8080);

        // 获取输出流
        OutputStream outputStream = socket.getOutputStream();

        // 获取标准输入
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入3行数据：");

        // 输入3行数据
        for (int i = 0; i < 3; i++) {
            String msg = scanner.nextLine();
            outputStream.write(msg.getBytes("utf-8"));
            outputStream.flush();
        }
        scanner.close();

        // 为什么关闭后服务端才能收到数据
        outputStream.close();
    }
}
