//-------------------------------------------------------------------------------------------
// File:   Broker.java
// Author: Jorge Soria Romero (872016) y Jiahao Ye (875490)
// Date:   11 de abril de 2025
// Coms:   Fichero interfaz de la clase Broker, de la práctica 3 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.rmi.RemoteException;
import java.io.Serializable;
import java.util.ArrayList;
import java.rmi.Remote;

public interface Broker extends Remote {

    /*----------------------------------------------------------------------------------*
     * API para los servidores                                                          *
     *----------------------------------------------------------------------------------*/

    /*
     * Pre:  Dado dos cadenas de carácteres "nombre_servidor" y "host_remoto_IP_puerto".
     * Post: Procedimiento que registra la existencia de un servidor con los parámetros
     *       incluidos. Si ya existía el servidor con "nombre_servidor", se deja el estado
     *       del Broker igual.
     */
    public void registrar_servidor(String nombre_servidor, String host_remoto_IP_puerto)
        throws RemoteException;

    /*
     * Pre:  Dado "nombre_servidor", que indica el nombre de un servidor registrado y la
     *       información correspondiente a un servicio (del servidor que se adjunta): nombre,
     *       parámetros y tipo de retorno. El servicio pasado es único dentro del servidor.
     * Post: El siguiente procedimiento almacena la relación de correspondencia del servicio
     *       al servidor "nombre_servidor".
     */
    public void alta_servicio(String nombre_servidor, String nom_servicio,
                              ArrayList<String> lista_param, String tipo_retorno) throws RemoteException;

    /*
     * Pre:  Dado "nombre_servidor", que indica el nombre de un servidor registrado y el
     *       nombre de un servicio, asignada al servidor.
     * Post: Este procedimiento da de baja el servicio del servidor, en el Broker.
     */
    public void baja_servicio(String nombre_servidor, String nom_servicio)
        throws RemoteException;

    /*----------------------------------------------------------------------------------*
     * API para los clientes                                                            *
     *----------------------------------------------------------------------------------*/

    /*
     * Pre:
     * Post: Función que devuelve todos los servicios registrados actualmente.
     */
    public ArrayList<Servicio> lista_servicios() throws RemoteException;

    /*
     * Pre:  Dado un nombre de un servicio y los parámetros requeridos para ejecutarlo.
     * Post: La siguiente función ejecuta el servicio de forma síncrona.
     */
    public Serializable ejecutar_servicio(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException;

    /*
     * Pre:  Dado un nombre de un servicio y los parámetros requeridos para ejecutarlo.
     * Post: El siguiente procedimiento ejecuta el servicio de forma asíncrona.
     */
    public void ejecutar_servicio_asinc(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException;

    /*
     * Pre:  Dado una cadena de carácteres "nom_servicio".
     * Post: La siguiente función obtiene el resultado de la llamada asíncrona de "nom_servicio".
     */
    public Serializable obtener_respuesta_asinc(String nom_servicio) throws RemoteException;    
}