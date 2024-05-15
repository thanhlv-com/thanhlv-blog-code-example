package com.thanhlv.test.queue;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.thanhlv.test.Constant;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Producer {
    private final static String QUEUE_NAME = "demo-queue";

    public static void main(String[] argv) throws Exception {
        System.out.println("Create a ConnectionFactory");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constant.HOST);
        factory.setPort(Constant.PORT);
        factory.setUsername(Constant.USER);
        factory.setPassword(Constant.PASSWORD);

        System.out.println("Create a Connection");
        System.out.println("Create a Channel");
        try ( Connection connection = factory.newConnection();
              Channel channel = connection.createChannel() ) {
            System.out.println("Create a queue " + QUEUE_NAME);
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            System.out.println("Start sending messages ... ");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));) {
                String message;
                do {
                    System.out.print("Enter message: ");
                    message = br.readLine().trim();
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                    System.out.println(" [x] Sent: '" + message + "'");
                } while (!message.equalsIgnoreCase("close"));
            }
        } finally {
            System.out.println("Close connection and free resources");
        }
    }

}