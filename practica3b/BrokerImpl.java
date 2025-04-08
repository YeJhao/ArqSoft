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
import java.util.Map;
import java.util.Set;

public class BrokerImpl extends UnicastRemoteObject implements Broker {
    
    // Se suponen los nombres de servidores únicos
    // No puede haber dos servicios iguales en dos servidores distintos
    public Map<String, Set<Servicio>> servicios;
    public Map<String, String> servidores;

    public BrokerImpl() throws RemoteException {
        super();
        servicios = new HashMap<>();
        servidores = new HashMap<>();
    }
    
    /*----------------------------------------------------------------------------------*
     * API para los servidores                                                          *
     *----------------------------------------------------------------------------------*/

    /** Registar servidor */
    @Override
    public void registrar_servidor(String nombre_servidor, String host_remoto_IP_puerto)
        throws RemoteException {
        servicios.computeIfAbsent(nombre_servidor, k -> new HashSet<>());
        servidores.put(nombre_servidor, host_remoto_IP_puerto);
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
        Set<Servicio> serv = servicios.get(nombre_servidor);
        if (serv != null) {
            Iterator<Servicio> it = serv.iterator();
            boolean encontrado = false;
            while (it.hasNext() && !encontrado) {
                Servicio s = it.next();
                if (s.nom_servicio.equals(nom_servicio)) {
                    encontrado = true;
                    it.remove();
                }
            }
        }
    }

    /*----------------------------------------------------------------------------------*
     * API para los clientes                                                            *
     *----------------------------------------------------------------------------------*/

    /** Listar servicios actualmente registrados */
    @Override
    public ArrayList<Servicio> lista_servicios()
        throws RemoteException {


        // Con ArrayList
        /* 
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
        
        ArrayList<Servicio> lista = new ArrayList<>();

        // Obtener el Set<Servicio> asociado a la clave 'server'
        Set<Servicio> serviciosSet = servicios.get(server);

        // Convertir el Set<Servicio> a un arreglo de Servicio[]
        Servicio[] serviciosArray = serviciosSet.toArray(new Servicio[0]);

                // Recorrer todas las claves del mapa
        for (String server : servicios.keySet()) {
            // Obtener el Set<Servicio> para cada servidor
            Set<Servicio> servicioSet = servicios.get(server);
            
            // Recorrer cada servicio en el Set<Servicio> y añadirlo a la lista
            for (Servicio servicio : servicioSet) {
                lista.append(servicio).append("\n"); // Asumiendo que el método toString() de Servicio está bien implementado
            }
        }
        return null;
    }

    /** Ejecutar un servicio de forma síncrona */
    @Override
    public Serializable ejecutar_servicio(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException {

        

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
}