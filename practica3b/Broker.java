import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Broker extends Remote {

    /*----------------------------------------------------------------------------------*
     * API para los servidores                                                          *
     *----------------------------------------------------------------------------------*/

    /** Registar servidor */
    public void registrar_servidor(String nombre_servidor, String host_remoto_IP_puerto)
        throws RemoteException;

    /** Registrar un nuevo servicio */
    public void alta_servicio(String nombre_servidor, String nom_servicio,
        ArrayList<String> lista_param, String tipo_retorno) throws RemoteException;

    /** Dar de baja un servicio */
    public void baja_servicio(String nombre_servidor, String nom_servicio)
        throws RemoteException;

    /*----------------------------------------------------------------------------------*
     * API para los clientes                                                            *
     *----------------------------------------------------------------------------------*/

    /** Listar servicios actualmente registrados */
    public ArrayList<Servicio> lista_servicios()
        throws RemoteException;

    /** Ejecutar un servicio de forma síncrona */
    public Object ejecutar_servicio(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException;

    /** Ejecutar un servicio de manera asíncrona */
    public void ejecutar_servicio_asinc(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException;

    /** Obtener respuesta de una ejecución asíncrona */
    public Object obtener_respuesta_asinc(String nom_servicio)
        throws RemoteException;    
}