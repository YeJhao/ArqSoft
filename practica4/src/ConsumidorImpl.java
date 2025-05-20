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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumidorImpl extends UnicastRemoteObject implements Consumidor {

    private final String nombre;
    private final Broker broker;
    private final String queueName;
    private final Consumer<String> callbackFunction;

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

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
    @Override
    public void callback(String msg) throws RemoteException {
        executor.submit(() -> {
            try {
                if (callbackFunction != null) {
                    callbackFunction.accept(msg);
                }

                System.out.println(nombre + " -> ACK enviado tras procesar: \"" + msg + "\"");
                broker.acknowledgement(queueName, msg, this.nombre);

            } catch (Exception e) {
                System.err.println("Error en procesamiento o ACK: " + e.getMessage());
            }
        });           
    }

    /**
     * Pre: ---
     * Post: Devuelve una cadena para identificar al consumidor
     */
    @Override
    public String getId() throws RemoteException {
        return this.nombre;
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
                int tiempoTotal = 5 + rand.nextInt(11); // entre 5 y 15 segundos
                String[] puntos = {"", ".", "..", "..."};
                System.out.println("Procesando mensaje: \"" + mensaje + "\"");
                for (int i = 1; i <= tiempoTotal; i++) {
                    String animacion = puntos[i % puntos.length];
                    System.out.print(String.format("\rTiempo: %2d/%2d segundos %s", i, tiempoTotal, animacion));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.err.println("\nProcesamiento interrumpido.");
                        return;
                    }
                }
                System.out.println("\nProcesamiento finalizado para: \"" + mensaje + "\"");
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