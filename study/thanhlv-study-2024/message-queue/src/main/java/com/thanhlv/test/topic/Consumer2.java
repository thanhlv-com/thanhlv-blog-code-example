package com.thanhlv.test.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.thanhlv.test.Constant;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Consumer2 {
    public static void main(String[] args) throws Exception {
        System.out.println("Create a ConnectionFactory");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constant.HOST);
        factory.setPort(Constant.PORT);
        factory.setUsername(Constant.USER);
        factory.setPassword(Constant.PASSWORD);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
//
            // Create topic exchange
            channel.exchangeDeclare(ConstantTopic.TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC, true);

            // Create queues
            channel.queueDeclare(ConstantTopic.TOPIC_QUEUE_1, true, false, false, null);
            channel.queueDeclare(ConstantTopic.TOPIC_QUEUE_3, true, false, false, null);

            // Binding queues with routing key
            channel.queueBind(ConstantTopic.TOPIC_QUEUE_1, ConstantTopic.TOPIC_EXCHANGE, ConstantTopic.ROUTING_KEY_TOPIC_QUEUE_1
            );
            channel.queueBind(ConstantTopic.TOPIC_QUEUE_3, ConstantTopic.TOPIC_EXCHANGE, ConstantTopic.ROUTING_KEY_TOPIC_QUEUE_1
            );

            channel.basicConsume(ConstantTopic.TOPIC_QUEUE_3, true, ((consumerTag, message) -> {
                System.out.println("[Received] [" + ConstantTopic.TOPIC_QUEUE_3 + "]: " + consumerTag);
                System.out.println("[Received] [" + ConstantTopic.TOPIC_QUEUE_3 + "]: " + new String(message.getBody()));
            }), consumerTag -> {
                System.out.println(consumerTag);
            });
            channel.basicConsume(ConstantTopic.TOPIC_QUEUE_1, true, ((consumerTag, message) -> {
                System.out.println("[Received] [" + ConstantTopic.TOPIC_QUEUE_1 + "]: " + consumerTag);
                System.out.println("[Received] [" + ConstantTopic.TOPIC_QUEUE_1 + "]: " + new String(message.getBody()));
            }), consumerTag -> {
                System.out.println(consumerTag);
            });

            // keep not kill app
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));) {
                String message;
                do {
                    message = br.readLine().trim();
                } while (!message.equalsIgnoreCase("close"));
            }

        } finally {
            System.out.println("Close connection and free resources");
        }

    }
}
