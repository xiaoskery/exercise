package com.study.demo;

import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.SourcePollingChannelAdapterSpec;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@SpringBootApplication
@EnableJms
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @JmsListener(destination = "queue1")
    public void receive(String message) {
        System.out.println("收到消息：" + message);
    }

    @PostConstruct
    public void init() {

    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] {"tcp://114.67.98.170:1883"});
        options.setUserName("admin");
        options.setPassword("admin".toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public IntegrationFlow mqttOutFlow() {
        return IntegrationFlows.from(() -> "hello mqtt", new Consumer<SourcePollingChannelAdapterSpec>() {
            public void accept(SourcePollingChannelAdapterSpec sourcePollingChannelAdapterSpec) {
                sourcePollingChannelAdapterSpec.poller(Pollers.fixedDelay(1000));
            }
        }).transform(p -> p + "send to mqtt").handle(mqttOutbound()).get();
    }

    @Bean
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("client-si-producer-0", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("x/y/z");
        return messageHandler;
    }

    // consumer
    @Bean
    public IntegrationFlow mqttInFlow() {
        return IntegrationFlows.from(mqttInbound()).transform(p -> p + ", received from MQTT").handle(printHandler())
            .get();
    }

    private MessageHandler printHandler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println(message.getPayload().toString());
            }
        };
    }

    @Bean
    public MessageProducerSupport mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
            new MqttPahoMessageDrivenChannelAdapter("client-si-consumer-1", mqttClientFactory(), "x/y/z");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        return adapter;
    }
}
