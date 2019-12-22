package com.study.demo;

import java.util.Date;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    public static void main(String[] args) {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 设置连接信息
        factory.setHost("114.67.98.170");
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection conn = null;
        Channel channel = null;
        try {
            // 创建连接
            conn = factory.newConnection("生产者");

            // 创建通道
            channel = conn.createChannel();

            // 声明创建队列
            channel.queueDeclare("queue1", false, false, false, null);

            // 发送消息
            String message = "hello world !!!" + new Date().toString();
            for (int i = 0; i < 1000; i++) {
                channel.basicPublish("", "queue1", null, message.getBytes());
                System.out.println("消息已发送");
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
