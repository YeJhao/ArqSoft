//-------------------------------------------------------------------------------------------
// File:   Consumidor.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   6 de mayo de 2025
// Coms:   Fichero interfaz de la clase Consumidor, de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Consumidor extends Remote {

    /**
     * Pre:  El mensaje proviene de una cola a la que el consumidor está suscrito.
     * Post: El consumidor procesa el mensaje recibido. Debería responder
     *       con un ACK al broker tras procesar exitosamente el mensaje.
     */
    public void callback(String msg) throws RemoteException;
}