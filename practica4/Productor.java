//-------------------------------------------------------------------------------------------
// File:   Productor.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   29 de abril de 2025
// Coms:   Fichero java de la clase Productor, de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.nio.channels.Channel;
import java.sql.Connection;

public class Productor {
    private final static String QUEUE_NAME = "hello";
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                String message = "Hello World!";
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println(" [x] Sent '" + message + "'");
        }
    }
    
}
