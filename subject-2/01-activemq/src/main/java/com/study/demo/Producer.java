package com.study.demo;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {
    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory = null;
        Connection conn = null;
        Session session = null;
        try {
            // 1.连接工厂
            connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://114.67.98.170:61616");

            // 2.连接
            conn = connectionFactory.createConnection();
            conn.start();

            // 3.回话
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // 4.目的地
            Destination destination = session.createQueue("queue1");

            // 5.生产者
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // 6.消息对象
            String text = "hello world !!!";
            TextMessage textMessage = session.createTextMessage(text);

            // 7.发送消息
            for (int i = 0; i < 1000; i++) {
                producer.send(textMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
