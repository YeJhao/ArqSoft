//-------------------------------------------------------------------------------------------
// File:   ConsumidorImpl.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   6 de mayo de 2025
// Coms:   Fichero java ConsumidorImpl, de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.function.Consumer;
import java.util.Random;

public class ConsumidorImpl extends UnicastRemoteObject implements Consumidor {

    private final String nombre;
    private final Broker broker;
    private final String queueName;
    private final Consumer<String> callbackFunction;

    /** Constructor */
    public ConsumidorImpl(String nombre, Broker broker, String queueName, Consumer<String> callbackFunction)
        throws RemoteException {
        super();
        this.nombre = nombre;
        this.broker = broker;
        this.queueName = queueName;
        this.callbackFunction = callbackFunction;
    }

    /**
     * Pre:  El mensaje proviene de una cola a la que el consumidor está suscrito.
     * Post: El consumidor procesa el mensaje recibido. Debería responder
     *       con un ACK al broker tras procesar exitosamente el mensaje.
     */
    public void callback(String msg) throws RemoteException {
        System.out.println(nombre + "-> Mensaje recibido: \"" + msg + "\"");

        // Ejecutar la función de usuario
        if(callbackFunction != null) {
            callbackFunction.accept(msg);
        }

        // Al terminar enviar ACK
        broker.acknowledgement(queueName, msg);
        System.out.println(nombre + "-> ACK enviado tras procesar: \"" + msg + "\"");
    }

    /** Main del consumidor remoto */
    public static void main(String[] args) {
        System.setProperty("java.security.policy", "./java.policy");
        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        if(args.length < 3) {
            System.out.println("Uso: java ConsumidorImpl <nombreConsumidor> <nombreCola> <host:puerto>");
            return;
        }

        String nombre = args[0];
        String queue = args[1];
        String host = args[2];

        try {

            // Conectar con el broker de mensajes remoto
            Broker broker = (Broker) Naming.lookup("rmi://" + host + "/Broker");

            // Definir comportamiento del consumidor (callback)
            Consumer<String> logica = mensaje -> {
                Random rand = new Random();
                int tiempoTotal = 30 + rand.nextInt(61); // entre 30 y 90 segundos
                long tiempoFin = System.currentTimeMillis() + tiempoTotal * 1000L;
                String[] puntos = {"", ".", "..", "..."};
                int i = 0;
                try {
                    while(System.currentTimeMillis() < tiempoFin) {
                        System.out.println("Haciendo algo con el mensaje \"" + mensaje + "\"" + puntos[i % puntos.length]);
                        i++; Thread.sleep(3000); // Cada 3 segundos
                    }
                } catch (InterruptedException e) {
                    System.err.println("Procesamiento interrumpido.");
                }
            };

            // Crear consumidor remoto y asociarlo a la cola
            Consumidor consumidor = new ConsumidorImpl(nombre, broker, queue, logica);
            broker.consumir(queue, consumidor);
            System.out.println("Consumidor  \"" + nombre + "\" suscrito a cola \"" + queue + "\".");

        } catch(Exception e) {
            System.err.println("Error en ConsumidorImpl: " + e.getMessage());
            e.printStackTrace();
        }
    }
}