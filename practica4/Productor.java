//-------------------------------------------------------------------------------------------
// File:   Productor.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   6 de mayo de 2025
// Coms:   Fichero Productor.java, de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Clase Productor
 */
public class Productor {

    /** Programa principal: productor interactivo por terminal */
    public static void main(String[] args) {
        System.setProperty("java.security.policy", "./java.policy");
        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        if(args.length < 3) {
            System.out.println("Uso: java Productor <host:puerto> <cola> <mensaje>");
            return;
        }

        String dir = args[0];       // Host:puerto broker mensajes
        String queueName = args[1]; // Nombre cola
        String mensaje = args[2];   // Mensaje

        try {
            // Conexión con broker de mensajes remoto
            Broker broker = (Broker) Naming.lookup("rmi://" + dir + "/Broker");

            // Declarar cola (idempotente)
            try {
                broker.declarar_cola(queueName);
                System.out.println("Cola \"" + queueName + "\" declarada.");
            } catch(RemoteException e) {
                System.out.println("Cola \"" + queueName + "\" ya existe. Continuando.");
            }

            // Publicar el mensaje
            broker.publicar(queueName, mensaje);
            System.out.println("Mensaje publicado en [" + queueName + "]: \"" + mensaje + "\".");

        } catch(Exception e) {
            System.err.println("Error en Productor: " + e.getMessage());
            e.printStackTrace();
        }

    }
}