package com.study.demo;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer {

    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory = null;
        Connection conn = null;
        Session session = null;

        try {
            // 1.连接工厂
            connectionFactory = new ActiveMQConnectionFactory("tcp://114.67.98.170:61616");

            // 2.连接
            conn = connectionFactory.createConnection("admin", "admin");
            conn.start();

            // 3.回话
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // 4.目的地
            Destination destination = session.createQueue("queue1");

            // 5.消费者
            MessageConsumer consumer = session.createConsumer(destination);

            // 6.接收消息
            while (true) {
                Message message = consumer.receive(1000);
                if (null == message) {
                    Thread.sleep(50);
                    continue;
                }
                if (message instanceof TextMessage) {
                    System.out.println("收到消息：" + ((TextMessage)message).getText());
                } else {
                    System.out.println(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
