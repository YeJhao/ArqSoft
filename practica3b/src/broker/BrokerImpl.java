//-------------------------------------------------------------------------------------------
// File:   Broker.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   11 de abril de 2025
// Coms:   Fichero implementación de la clase Broker, de la práctica 3 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

package broker;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrokerImpl extends UnicastRemoteObject implements Broker {

    // Los nombres de servidores son únicos
    // No puede haber dos servicios iguales en dos servidores distintos

    /** Pares { nom_servidor, host_remoto_IP_puerto } */
    private final Map<String, String> servidores;

    /** Pares { nom_servicio, ServicioInfo } */
    private final Map<String, ServicioInfo> servicios;

    /** Constructor */
    public BrokerImpl() throws RemoteException {
        super();
        servidores = new HashMap<>();
        servicios = new HashMap<>();
    }

    public static class ServicioInfo implements Serializable {
        public final String nom_servidor;
        public final String nom_servicio;
        public final ArrayList<String> lista_param;
        public final String tipo_retorno;
        public final String url;

        public ServicioInfo(String nom_servidor, String nom_servicio, ArrayList<String> lista_param, String tipo_retorno, String url) {
            this.nom_servidor = nom_servidor;
            this.nom_servicio = nom_servicio;
            this.lista_param = lista_param;
            this.tipo_retorno = tipo_retorno;
            this.url = url;
        }
    }

    /*
     * Pre:  Dado dos cadenas de carácteres "nombre_servidor" y "host_remoto_IP_puerto".
     * Post: Procedimiento que registra la existencia de un servidor con los parámetros
     *       incluidos. Si ya existía el servidor con "nombre_servidor", se deja el estado
     *       del Broker igual.
     */
    @Override
    public void registrar_servidor(String nombre_servidor, String host_remoto_IP_puerto)
        throws RemoteException {
        
        if(!servidores.containsKey(nombre_servidor)) {
            servidores.put(nombre_servidor, host_remoto_IP_puerto);
            System.out.println("Servidor registrado: " + nombre_servidor + " -> " + host_remoto_IP_puerto);
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

        String url = servidores.get(nombre_servidor);
        if(url == null) {
            System.out.println("Servidor no registrado: " + nombre_servidor);
            return;
        }
        ServicioInfo sinfo = new ServicioInfo(
            nombre_servidor, nom_servicio, lista_param, tipo_retorno, url);
        servicios.put(nom_servicio, sinfo);
        System.out.println("Alta de servicio \"" + nom_servicio + "\" del servidor \"" + nombre_servidor + "\".");
    }

    /*
     * Pre:  Dado "nombre_servidor", que indica el nombre de un servidor registrado y el
     *       nombre de un servicio, asignada al servidor.
     * Post: Este procedimiento da de baja el servicio del servidor, en el Broker.
     */
    @Override
    public void baja_servicio(String nombre_servidor, String nom_servicio)
        throws RemoteException {

        ServicioInfo sinfo = servicios.get(nom_servicio);
        if(sinfo != null && sinfo.nom_servidor.equals(nombre_servidor)) {
            servicios.remove(nom_servicio);
            System.out.println("Baja del servicio \"" + nom_servicio + "\" del servidor \"" + nombre_servidor + "\".");
        } else {
            System.out.println("Servicio no encontrado o no pertenece al servidor \"" +  nombre_servidor + "\".");
        }
    }

    /*----------------------------------------------------------------------------------*
     * API para los clientes                                                            *
     *----------------------------------------------------------------------------------*/

    /*
     * Pre:
     * Post: Función que devuelve el nombre de todos los servicios registrados.
     */
    @Override
    public Servicios lista_servicios() throws RemoteException {
        return new Servicios(servicios.keySet());
    }

    /*
     * Pre:  Dado un nombre de un servicio y los parámetros requeridos para ejecutarlo.
     * Post: La siguiente función ejecuta el servicio de forma síncrona.
     */
    @Override
    @SuppressWarnings("UseSpecificCatch")
    public Respuesta<Object> ejecutar_servicio(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException {

        ServicioInfo sinf = servicios.get(nom_servicio);
        if(sinf == null) {
            return new Respuesta<>("Error: Servicio \"" + nom_servicio + "\" no registrado.");
        }
        try {
            Remote remote = Naming.lookup("rmi://" + sinf.url);
            Method[] methods = remote.getClass().getMethods();
            for(Method method : methods) {
                if(method.getName().equals(nom_servicio) && method.getParameterCount() == parametros_servicio.size()) {
                    Object resultado = method.invoke(remote, parametros_servicio);
                    return new Respuesta<>(resultado);
                }
            }
            return new Respuesta<>("Error: Método no encontrado en objeto remoto para servicio \"" + nom_servicio + "\".");
        } catch (Exception e) {
            return new Respuesta<>("Error: " + e.getMessage());
        }
    }

    /*
     * Pre:  Dado un nombre de un servicio y los parámetros requeridos para ejecutarlo.
     * Post: El siguiente procedimiento ejecuta el servicio de forma asíncrona.
     */
    @Override
    public void ejecutar_servicio_asinc(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException {
        //TODO
    }

    /*
     * Pre:  Dado una cadena de carácteres "nom_servicio".
     * Post: La siguiente función obtiene el resultado de la llamada asíncrona de "nom_servicio".
     */
    @Override
    public Respuesta<Object> obtener_respuesta_asinc(String nom_servicio)
        throws RemoteException {
        //TODO
        return null;
    }

    /*----------------------------------------------------------------------------------*
     * Main                                                                             *
     *----------------------------------------------------------------------------------*/

    @SuppressWarnings("UseSpecificCatch")
    public static void main(String[] args) {
        if(args.length != 2) {
            System.err.println("Uso: java BrokerImpl <ip> <puerto>");
            System.exit(-1);
        }

        // Obtengo parámetros (ip y puerto donde correrá el broker central)
        String ip = args[0], puerto = args[1];
        String url = "//" + ip + ":" + puerto + "/Broker";

        // Establecer permisos y política de seguridad
        System.setProperty("java.security.policy", "./java.policy");
        
        // Establecer securityManager. Función obsoleta, la ponemos
        // por qué así sale en los pdfs de las prácticas
        System.setSecurityManager(new SecurityManager());

        try {
            BrokerImpl broker = new BrokerImpl();
            Naming.rebind(url, broker);
            System.out.println("¡Estoy regitrado! Broker disponible en " + url);
        } catch (Exception e) {
            System.err.println("Error en Broker: " + e.getMessage());
        }
    }
}