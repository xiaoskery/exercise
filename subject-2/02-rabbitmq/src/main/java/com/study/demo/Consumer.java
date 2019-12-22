package com.study.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer {
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
            conn = factory.newConnection("消费者");

            // 创建通道
            channel = conn.createChannel();

            // 声明创建队列
            channel.queueDeclare("queue1", false, false, false, null);

            // 收到消息的回调
            DeliverCallback callback = (consumerTag, message) -> {
                System.out.println("收到消息：" + new String(message.getBody(), "UTF-8"));
            };

            // 监听队列
            channel.basicConsume("queue1", true, "queue1", callback, (consumerTag) -> {
            });
            System.out.println("开始接收消息");
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
