//-------------------------------------------------------------------------------------------
// File:   Productor.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   29 de abril de 2025
// Coms:   Fichero java de la clase Consumidor, de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.nio.channels.Channel;
import java.sql.Connection;

public class Consumidor {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    }

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
    };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
}
