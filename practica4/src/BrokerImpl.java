//-------------------------------------------------------------------------------------------
// File:   BrokerImpl.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   19 de mayo de 2025
// Coms:   Fichero implementación de la clase Broker, de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;

public class BrokerImpl extends UnicastRemoteObject implements Broker, Serializable {
    private final Map<String, Cola> colas = new ConcurrentHashMap<>();

    /** Constructor */
    public BrokerImpl() throws RemoteException {
        super();
        cargarColasExistentes();       // Parte voluntaria: Message durability
        iniciarReintentoPeriodico();   // Parte voluntaria: ACKs y reenviar mensajes
    }

    /*
     * Pre:  Dado una cadena de caracteres "queueName" identificando una cola que se quiere crear.
     * Post: Procedimieno que crea una cola con "queueName" si no existe, en caso contrario
     *       no hace nada.
     */
    @Override
    public void declarar_cola(String queueName) throws RemoteException {
        if (colas.containsKey(queueName)) {
            throw new RemoteException("La cola \"" + queueName + "\" ya está declarada.");
        }
        colas.put(queueName, new Cola(queueName));
    }

    /*
     * Pre:  Dado dos cadenas de caracteres, una identificación de la cola y un mensaje.
     * Post: Procedimiento que almacena "msg" en la cola con nombre "queueName".
     */
    @Override
    public void publicar(String queueName, String msg) throws RemoteException {
        Cola cola = colas.get(queueName);

        if (cola == null) {
            throw new RemoteException("No existe la cola \"" + queueName + "\".");
        }
        
        cola.encolar(msg);
        entregarPendiente(cola);
    }

    /*
     * Pre:  Dada uns cola identificada por "queueName" que ha sido declarada previamente.
     * Post: Registra al objeto consumidor remoto como suscriptor de la cola. El broker invocará
     *       su método callback cuando lleguen mensajes nuevos y siguiendo política fair dispatch.
     */
    @Override
    public void consumir(String queueName, Consumidor consumidor) throws RemoteException {
        Cola cola = colas.get(queueName);

        if (cola == null) {
            throw new RemoteException("No existe la cola \"" + queueName + "\".");
        }

        cola.registrarConsumidor(consumidor);
        entregarPendiente(cola);
    }

    /*
     * Pre:  ---
     * Post: Devuelve un listado con los nombres de las colas disponibles. Si no hay
     *       ninguna cola disponible, devuelve una lista vacía.
     */
    @Override
    public ArrayList<String> listar_colas() throws RemoteException {
        return new ArrayList<>(colas.keySet());
    }

    /*
     * Pre:  ---
     * Post: El broker elimina el mensaje "msg" del registro de mensajes pendientes en la cola
     *       "queueName", considerándolo entregado correctamente. Este proceso completa la entrega
     *       del mensaje. Si el mensaje no estaba pendiente, la operación no tiene efecto.
     */
    @Override
    public void acknowledgement(String queueName, String msg, String consumidorId) throws RemoteException {
        Cola cola = colas.get(queueName);

        if (cola == null) {
            throw new RemoteException("No existe la cola \"" + queueName + "\".");
        }

        if (cola.confirmarACK(msg, consumidorId)) {
            System.out.println("ACK para mensaje \"" + msg + "\" recibido de consumidor \"" + consumidorId + "\" en cola \"" + queueName + "\"");
            entregarPendiente(cola);
        } else {
            System.err.println("No se pudo confirmar ACK para mensaje \"" + msg + "\" de consumidor \"" + consumidorId + "\" en cola \"" + queueName + "\".");
        }
    }

    /******************************
     * Métodos privados
     ******************************/

    /*
     * Pre:  La cola ha sido declarada y tiene consumidores registrados.
     * Post: Intenta entregar un mensaje a cada consumidor disponible. Si lo consigue,
     *       marca el mensaje como pendiente de ACK y lo envía mediante callback remoto.
     */
    /*private void entregarPendiente(Cola cola) {
        for(Consumidor c : cola.getConsumidoresDisponibles()) {
            try {
                String msg = cola.entregarMensaje(c);

                if (msg != null) {
                    c.callback(msg);
                }
                return;
            }
            catch(RemoteException e) {
                System.err.println("Error entregando mensaje: " + e.getMessage());
            }
        }
    }*/
    private void entregarPendiente(Cola cola) {
        synchronized (cola) {
            boolean entregado;
            do {
                entregado = false;
                for (Consumidor c : cola.getConsumidoresDisponibles()) {
                    try {
                        String msg = cola.entregarMensaje(c);
                        if (msg != null) {
                            c.callback(msg);
                            entregado = true;
                        }
                    } catch (RemoteException e) {
                        System.err.println("Error entregando mensaje: " + e.getMessage());
                    }
                }
            } while (entregado);
        }
    }

    /*
     * Pre:  ---
     * Post: Carga del disco todas las colas previamente persistidas en el
     *       directorio "data/" para restaurar su estado (message durability).
     */
    private void cargarColasExistentes() {
        File carpeta = new File("data");

        if(!carpeta.exists()) carpeta.mkdir();
        
        for (File f : carpeta.listFiles()) {
            if(f.getName().startsWith("cola_") && f.getName().endsWith(".dat")) {
                String nombre = f.getName().substring(5, f.getName().length() - 4);
                colas.put(nombre, new Cola(nombre));
            }
        }
    }

    /*
     * Pre:  ---
     * Post: Inicia un hilo programado que, cada 10 segundos, revisa los mensajes
     *       entregados que no han recibido ACK. Si alguno ha expirado, se reencola y
     *       se reenvia a un consumidor no ocupado
     */
    private void iniciarReintentoPeriodico() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(()->{
            for (Cola cola : colas.values()) {
                List<String> reentregas = cola.recuperarMensajesExpirados();
                
                if(!reentregas.isEmpty()) {
                    entregarPendiente(cola);
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    /******************************
     * Main
     ******************************/

    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("Uso: java BrokerImpl <host:puerto>");
            return;
        }

        String dir = args[0];
        String url = "//" + dir + "/Broker";

        System.setProperty("java.security.policy", "./java.policy");
        System.setSecurityManager(new SecurityManager());

        try {
            BrokerImpl broker = new BrokerImpl();
            Naming.rebind(url, broker);
            System.out.println("Broker disponible en " + url);
        } catch(Exception e) {
            System.err.println("Error al registrar el broker: " + e.getMessage());
            e.printStackTrace();
        }
    }
}