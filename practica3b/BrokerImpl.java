//-------------------------------------------------------------------------------------------
// File:   Broker.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   01 de abril de 2025
// Coms:   Fichero implementación de la clase Broker, de la práctica 3 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.rmi.Naming;
import java.util.Map;
import java.util.Set;

public class BrokerImpl extends UnicastRemoteObject implements Broker {
    
    // Se suponen los nombres de servidores únicos
    // No puede haber dos servicios iguales en dos servidores distintos
    public Map<Servidor, Set<Servicio>> servicios;
    public Map<String, String> servidores;

    public BrokerImpl() throws RemoteException {
        super();
        servicios = new HashMap<Servidor, Set<Servicio>>();
        servidores = new HashMap<>();
    }
    
    /*----------------------------------------------------------------------------------*
     * API para los servidores                                                          *
     *----------------------------------------------------------------------------------*/

    /** Registar servidor */
    @Override
    public void registrar_servidor(String nombre_servidor, String host_remoto_IP_puerto)
        throws RemoteException {
        
        // Intentamos insertar nuevo servidor
        if (servidores.computeIfAbsent(nombre_servidor, k -> host_remoto_IP_puerto).equals(host_remoto_IP_puerto)) {
            // Si no existía tal servidor
            servicios.put(new Servidor(nombre_servidor, host_remoto_IP_puerto), new HashSet<>());
        }
    }

    /** Registrar un nuevo servicio */
    @Override
    public void alta_servicio(String nombre_servidor, String nom_servicio,
        ArrayList<String> lista_param, String tipo_retorno) throws RemoteException {
        Servicio servicio = new Servicio(nom_servicio, nombre_servidor, lista_param, tipo_retorno);
        servicios.get(nombre_servidor).add(servicio);
    }

    /** Dar de baja un servicio */
    @Override
    public void baja_servicio(String nombre_servidor, String nom_servicio)
        throws RemoteException {

        if(servicios.containsKey(nombre_servidor))
            servicios.remove(nom_servicio);
    }

    /*----------------------------------------------------------------------------------*
     * API para los clientes                                                            *
     *----------------------------------------------------------------------------------*/

    /** Listar servicios actualmente registrados */
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

    /** Ejecutar un servicio de forma síncrona */
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


    /** Ejecutar un servicio de manera asíncrona */
    @Override
    public void ejecutar_servicio_asinc(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException {

    }

    /** Obtener respuesta de una ejecución asíncrona */
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