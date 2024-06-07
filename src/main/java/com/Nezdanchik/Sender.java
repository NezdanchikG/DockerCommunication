package com.Nezdanchik;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Sender {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        if (argv.length < 2) {
            System.err.println("Usage: Sender [topic] [message]");
            System.exit(1);
        }

        // Добавление задержки 20 секунд
        Thread.sleep(20000);

        String topic = argv[0];
        String message = argv[1];

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            System.out.println(" [x] Exchange declared: " + EXCHANGE_NAME);

            channel.basicPublish(EXCHANGE_NAME, topic, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + topic + "':'" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            System.out.println(" [x] Exchange declared: " + EXCHANGE_NAME);

            channel.basicPublish(EXCHANGE_NAME, topic, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + topic + "':'" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}