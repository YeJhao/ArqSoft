//-------------------------------------------------------------------------------------------
// File:   BrokerImpl.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   6 de mayo de 2025
// Coms:   Fichero implementación de la clase Broker, de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BrokerImpl implements Broker {
    // Map de colas? (nombre, cola correspondiente), no sería necesario singleton
    // si tenemos el nombre de la cola, el valor (cola) existe
    // Clase cola?
    private final ConcurrentMap<String, Cola> colas;

    private BrokerImpl() {
        colas = new ConcurrentHashMap<>();
    }

    /*
     * Pre:  Dado una cadena de caracteres "queueName" identificando una cola que se quiere crear
     * Post: Procedimieno que crea una cola con "queueName" si no existe, en caso contrario
     *       no hace nada.
     */
    @Override
    public void declarar_cola(String queueName) throws RemoteException {
        colas.computeIfAbsent(queueName, name -> {
            System.out.println("Cola creada: " + name);
            return new Cola();  // Crea la nueva cola
        });
    }

    /*
     * Pre:  Dado dos cadenas de caracteres, una identificación de la cola y un mensaje.
     * Post: Procedimiento que almacena "msg" en la cola con nombre "queueName".
     */
    @Override
    public void publicar(String queueName, String msg) throws RemoteException {
        if(colas.containsKey(queueName)) {
            // meter mensaje string en queue
            // Si está lleno? (buffer para delayed)
        }
        else {
            // Descartar mensaje (no hacer nada?)
        }
    }


    @Override
    public String consumir(String queueName, Callback callback) throws RemoteException {
        if(colas.containsKey(queueName)) {
            Cola q = colas.get(queueName);

            // Obtener mensaje de la cola
        }
        else {
            System.err.println("No existe la cola " + queueName);
        }

        return null;
    }
}