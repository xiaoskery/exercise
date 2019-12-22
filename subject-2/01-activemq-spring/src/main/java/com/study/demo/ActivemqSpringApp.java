package com.study.demo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
public class ActivemqSpringApp {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ActivemqSpringApp.class, args);
    }

    @PostConstruct
    public void init() {
        jmsTemplate.convertAndSend("queue1", "Hello world i am from spring activemq.");
    }
}
