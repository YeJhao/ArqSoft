//-------------------------------------------------------------------------------------------
// File:   Broker.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   29 de abril de 2025
// Coms:   Fichero java de la clase Broker, de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Broker extends Remote {
    
    /*
     * Pre:  H
     * Post: Procedimieno que crea una cola con "queueName" si no existe, en caso contrario
     *       no hace nada.
     */
    public void declarar_cola(String queueName) throws RemoteException; 

    /*
     * Pre:  H
     * Post: Procedimiento que almacena "msg" en la cola con nombre "queueName".
     */
    public void publicar(String queueName, String msg) throws RemoteException;

    /*
     * Pre:  H
     * Post: El siguiente procedimiento extrae el mensaje de la cola declarada.
     */
    public void consumir(String queueName, Callback callback) throws RemoteException;
}
