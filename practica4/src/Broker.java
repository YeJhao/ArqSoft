//-------------------------------------------------------------------------------------------
// File:   Broker.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   19 de mayo de 2025
// Coms:   Fichero interfaz de la clase Broker, de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Broker extends Remote {

    /*
     * Pre:  Dado una cadena de caracteres "queueName" identificando una cola que se quiere crear.
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
     * Pre:  Dada uns cola identificada por "queueName" que ha sido declarada previamente.
     * Post: Registra al objeto consumidor remoto como suscriptor de la cola. El broker invocará
     *       su método callback cuando lleguen mensajes nuevos y siguiendo política fair dispatch.
     */
    public void consumir(String queueName, Consumidor consumidor) throws RemoteException;

    /*
     * Pre:  ---
     * Post: Devuelve un listado con los nombres de las colas disponibles. Si no hay
     *       ninguna cola disponible, devuelve una lista vacía.
     */
    public ArrayList<String> listar_colas() throws RemoteException;

    /*
     * Pre:  ---
     * Post: El broker elimina el mensaje "msg" del registro de mensajes pendientes en la cola
     *       "queueName", considerándolo entregado correctamente. Este proceso completa la entrega
     *       del mensaje. Si el mensaje no estaba pendiente, la operación no tiene efecto.
     */
    public void acknowledgement(String queueName, String msg, String consumidorId) throws RemoteException;
}