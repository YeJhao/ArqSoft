//-------------------------------------------------------------------------------------------
// File:   Broker.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   6 de mayo de 2025
// Coms:   Fichero interfaz de la clase Broker, de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Broker extends Remote {
    
    /*
     * Pre:  Dado una cadena de caracteres "queueName" identificando una cola que se quiere crear
     * Post: Procedimieno que crea una cola con "queueName" si no existe, en caso contrario
     *       no hace nada.
     */
    public void declarar_cola(String queueName) throws RemoteException; 

    /*
     * Pre:  Dado dos cadenas de caracteres, una identificación de la cola y un mensaje.
     * Post: Procedimiento que almacena "msg" en la cola con nombre "queueName".
     */
    public void publicar(String queueName, String msg) throws RemoteException;

    /*
     * Pre:  H
     * Post: El siguiente procedimiento extrae el mensaje de la cola declarada.
     */
    public String consumir(String queueName, Callback callback) throws RemoteException;
}
