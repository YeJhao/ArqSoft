//-------------------------------------------------------------------------------------------
// File:   Broker.java
// Author: Jorge Soria Romero (872016) y Jiahao Ye (875490)
// Date:   11 de abril de 2025
// Coms:   Fichero implementación de la clase Broker, de la práctica 3 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.Serializable;
import java.rmi.Naming;
import java.util.*;

public class BrokerImpl extends UnicastRemoteObject implements Broker {
    
    // Se suponen los nombres de servidores únicos
    // No puede haber dos servicios iguales en dos servidores distintos

    // A cada nombre del servidor, se le asigna una serie de servicios que tiene
    public Map<String, Set<Servicio>> servicios;

    // Nombre del servidor, con su IP y puerto
    public Map<String, String> servidores;

    public BrokerImpl() throws RemoteException {
        super();
        servicios = new HashMap<>();
        servidores = new HashMap<>();
    }
    
    /*----------------------------------------------------------------------------------*
     * API para los servidores                                                          *
     *----------------------------------------------------------------------------------*/

    /*
     * Pre:  Dado dos cadenas de carácteres "nombre_servidor" y "host_remoto_IP_puerto".
     * Post: Procedimiento que registra la existencia de un servidor con los parámetros
     *       incluidos. Si ya existía el servidor con "nombre_servidor", se deja el estado
     *       del Broker igual.
     */
    @Override
    public void registrar_servidor(String nombre_servidor, String host_remoto_IP_puerto)
        throws RemoteException {
        
        // Intentamos insertar nuevo servidor
        if (servidores.computeIfAbsent(nombre_servidor, k -> host_remoto_IP_puerto).equals(host_remoto_IP_puerto)) {
            // Si no existía tal servidor
            servicios.put(nombre_servidor, new HashSet<>());
        }
    }

    /*
     * Pre:  Dado "nombre_servidor", que indica el nombre de un servidor registrado y la
     *       información correspondiente a un servicio (del servidor que se adjunta): nombre,
     *       parámetros y tipo de retorno. El servicio pasado es único dentro del servidor.
     * Post: El siguiente procedimiento almacena la relación de correspondencia del servicio
     *       al servidor "nombre_servidor".
     */
    @Override
    public void alta_servicio(String nombre_servidor, String nom_servicio,
        ArrayList<String> lista_param, String tipo_retorno) throws RemoteException {
        Servicio servicio = new Servicio(nom_servicio, nombre_servidor, lista_param, tipo_retorno);
        servicios.get(nombre_servidor).add(servicio);
    }

    /*
     * Pre:  Dado "nombre_servidor", que indica el nombre de un servidor registrado y el
     *       nombre de un servicio, asignada al servidor.
     * Post: Este procedimiento da de baja el servicio del servidor, en el Broker.
     */
    @Override
    public void baja_servicio(String nombre_servidor, String nom_servicio)
        throws RemoteException {

        if(servicios.containsKey(nombre_servidor))
            servicios.remove(nom_servicio);
    }

    /*----------------------------------------------------------------------------------*
     * API para los clientes                                                            *
     *----------------------------------------------------------------------------------*/

    /*
     * Pre:
     * Post: Función que devuelve todos los servicios registrados actualmente.
     */
    @Override
    public ArrayList<Servicio> lista_servicios()
        throws RemoteException {


        // Con ArrayList
        
        ArrayList<Servicio> todosLosServicios = new ArrayList<>();

        // Iteramos sobre el map y vamos agregando todos los servicios de cada Set
        for (Set<Servicio> servicioSet : servicios.values()) {
            todosLosServicios.addAll(servicioSet);
        }

        return todosLosServicios;

        // Con Set
        /*
        Set<Servicio> todosLosServicios = new HashSet<>();

        // Iteramos sobre el map y vamos agregando todos los servicios de cada Set
        for (Set<Servicio> servicioSet : servicios.values()) {
            todosLosServicios.addAll(servicioSet);
        }

        return todosLosServicios; */
    }

    /*
     * Pre:  Dado un nombre de un servicio y los parámetros requeridos para ejecutarlo.
     * Post: La siguiente función ejecuta el servicio de forma síncrona.
     */
    @Override
    public Serializable ejecutar_servicio(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException {

        for (Map.Entry<String, Set<Servicio>> entry : servicios.entrySet()) {
            for (Servicio servicio : entry.getValue()) {
                
                    if (servicio.nom_servicio.equals(nom_servicio)) {
                        String server = entry.getKey();
                        server.execute(nom_servicio, parametros_servicio);
                    }
                
            }
        }

        // Recorrer el hashmap servicios, encontrar el primer servidor con servicio
        // Llamar al servidor.eex

        return null;
    }

    /*----------------------------------------------------------------------------------*
     * API para los clientes: versión asíncrona                                         *
     *----------------------------------------------------------------------------------*/

    /*
     * Pre:  Dado un nombre de un servicio y los parámetros requeridos para ejecutarlo.
     * Post: El siguiente procedimiento ejecuta el servicio de forma asíncrona.
     */
    @Override
    public void ejecutar_servicio_asinc(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException {

    }

    /*
     * Pre:  Dado una cadena de carácteres "nom_servicio".
     * Post: La siguiente función obtiene el resultado de la llamada asíncrona de "nom_servicio".
     */
    @Override
    public Serializable obtener_respuesta_asinc(String nom_servicio)
        throws RemoteException {

        return null;
    }

    /*----------------------------------------------------------------------------------*
     * Main                                                                             *
     *----------------------------------------------------------------------------------*/
    private static final String ip = "32000";
    private static final String hostname = "l";

    public static void main(String args[]) {
        System.setProperty("java.security.policy", "./java.policy");
        String hostName = hostname + ip;        
        try {
            
            // Crear objeto remoto
            BrokerImpl broker = new BrokerImpl();
            System.out.println("¡Creado!");
            
            // Registrar el objeto remoto
            Naming.rebind("//" + hostName + "/MyBroker", broker);
            System.out.println("¡Estoy registrado!");
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }
}