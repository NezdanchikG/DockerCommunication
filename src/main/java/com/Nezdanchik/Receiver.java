package com.Nezdanchik;

import com.rabbitmq.client.*;

public class Receiver {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        if (argv.length < 1) {
            System.err.println("Usage: Receiver [topic]");
            System.exit(1);
        }

        // Добавление задержки 20 секунд
        Thread.sleep(20000);

        String topic = argv[0];

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            System.out.println(" [x] Exchange declared: " + EXCHANGE_NAME);

            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, topic);
            System.out.println(" [x] Queue bound: " + queueName + " to topic: " + topic);

            System.out.println(" [*] Waiting for messages with topic '" + topic + "'. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

            // Бесконечный цикл ожидания для предотвращения завершения программы
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}