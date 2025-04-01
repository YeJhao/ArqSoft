
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrokerImpl extends UnicastRemoteObject implements Broker, Serializable {
    
    public Map<String, String> servidores; // {nombre servidor, ip_puerto}
    public Map<String, Servicio> servicios;  // {nombre servicio, servicio}

    public BrokerImpl() throws RemoteException {
        super();
        servidores = new HashMap<>();
        servicios = new HashMap<>();
    }
    
    /*----------------------------------------------------------------------------------*
     * API para los servidores                                                          *
     *----------------------------------------------------------------------------------*/

    /** Registar servidor */
    @Override
    public void registrar_servidor(String nombre_servidor, String host_remoto_IP_puerto)
        throws RemoteException {

        if(!servidores.containsKey(nombre_servidor))
            servidores.put(nombre_servidor, host_remoto_IP_puerto);
    }

    /** Registrar un nuevo servicio */
    @Override
    public void alta_servicio(String nombre_servidor, String nom_servicio,
        ArrayList<String> lista_param, String tipo_retorno) throws RemoteException {

        if(!servicios.containsKey(nom_servicio) &&
            servidores.containsKey(nombre_servidor)) {

            servicios.put(nombre_servidor,
                new Servicio(nom_servicio, nombre_servidor, lista_param, tipo_retorno));
        }
    }

    /** Dar de baja un servicio */
    @Override
    public void baja_servicio(String nombre_servidor, String nom_servicio)
        throws RemoteException {

    }

    /*----------------------------------------------------------------------------------*
     * API para los clientes                                                            *
     *----------------------------------------------------------------------------------*/

    /** Listar servicios actualmente registrados */
    @Override
    public ArrayList<Servicio> lista_servicios()
        throws RemoteException {
        
        return new ArrayList(servicios.values());
    }

    /** Ejecutar un servicio de forma síncrona */
    @Override
    public Object ejecutar_servicio(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException {

        return null;
    }

    /** Ejecutar un servicio de manera asíncrona */
    @Override
    public void ejecutar_servicio_asinc(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException {

    }

    /** Obtener respuesta de una ejecución asíncrona */
    @Override
    public Object obtener_respuesta_asinc(String nom_servicio)
        throws RemoteException {

        return null;
    }
}