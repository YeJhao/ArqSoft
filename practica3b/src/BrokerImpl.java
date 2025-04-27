//-------------------------------------------------------------------------------------------
// File:   BrokerImpl.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   20 de abril de 2025
// Coms:   Fichero implementación de la clase Broker, de la práctica 3 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.lang.reflect.Method;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BrokerImpl extends UnicastRemoteObject implements Broker {

    // Los nombres de servidores y servicios son únicos

    /** Pares { nom_servidor, host_remoto_IP_puerto } */
    private final ConcurrentMap<String, String> servidores;

    /** Pares { nom_servicio, ServicioInfo } */
    private final ConcurrentMap<String, ServicioInfo> servicios;

    /** Resultado correspondiente a la ejecución asíncrona del servicio */
    private final ConcurrentMap<String, Future<Object>> resultadosAsinc;

    /** Executor service compartido para la reutilización de hilos */
    private final ExecutorService executor;

    /* 
     * Mapa que devuelve <<true>> si el resultado de una ejecución asíncrona
     * de un servicio ya ha sido devuelta al cliente. Devuelve <<false>> en caso contrario
     */
    private final ConcurrentMap<String, Boolean> respuestaEntregada;

    /** Constructor */
    public BrokerImpl() throws RemoteException {
        super();
        servidores = new ConcurrentHashMap<>();
        servicios = new ConcurrentHashMap<>();
        resultadosAsinc = new ConcurrentHashMap<>();
        respuestaEntregada = new ConcurrentHashMap<>();
        executor = Executors.newCachedThreadPool();
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
     *       parámetros, tipo de retorno y una descripción. El servicio pasado es único
     *       dentro del servidor.
     * Post: El siguiente procedimiento almacena la relación de correspondencia del servicio
     *       al servidor "nombre_servidor".
     */
    @Override
    public void alta_servicio(String nombre_servidor, String nom_servicio,
        ArrayList<String> lista_param, String tipo_retorno, String description) throws RemoteException {

        String url = servidores.get(nombre_servidor);
        if(url == null) {
            System.out.println("Servidor no registrado: " + nombre_servidor);
            return;
        }
        if (servicios.containsKey(nom_servicio)) {
            System.out.println("Advertencia: Sobrescribiendo servicio ya existente: \"" + nom_servicio + "\".");
        }
        ServicioInfo sinfo = new ServicioInfo(
            nombre_servidor, nom_servicio, lista_param, tipo_retorno, url, description);
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
        }
        else {
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
    public Map<String, ServicioInfo> lista_servicios() throws RemoteException {
        return new HashMap<>(servicios);
    }

    /*
     * Pre:  Dado un nombre de un servicio y los parámetros requeridos para ejecutarlo.
     * Post: La siguiente función ejecuta el servicio de forma síncrona.
     */
    @Override
    public Respuesta<Object> ejecutar_servicio(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException {

        ServicioInfo sinf = servicios.get(nom_servicio);
        if(sinf == null)
            throw new RemoteException("Error: Servicio \"" + nom_servicio + "\" no registrado.");
        
        try {
            Remote remote = Naming.lookup("rmi:" + sinf.url);
            Method[] methods = remote.getClass().getMethods();
            
            for(Method method : methods) {
                
                // No es el servicio que se desea ejecutar
                if(!method.getName().equals(nom_servicio))
                    continue;
                
                // No se han introducido los parámetros necesarios
                Class<?>[] paramTypes = method.getParameterTypes();
                if(paramTypes.length != parametros_servicio.size())
                    continue;

                boolean coincide = true;
                for(int i = 0; i < paramTypes.length; ++i) {
                    // Comprobar que los parámetros son del tipo adecuado
                    Object param = parametros_servicio.get(i);
                    String tipoEsperado = sinf.lista_param.get(i);
                    if(!tipoCoincide(paramTypes[i], tipoEsperado, param)) {
                        coincide = false;
                        break;
                    }
                }

                if(coincide) {
                    Object resultado = method.invoke(remote, parametros_servicio.toArray());
                    return new Respuesta<>(resultado);
                }
            }
            throw new RemoteException("Error: Método no encontrado en objeto remoto para servicio \"" + nom_servicio + "\".");
        }
        catch (Exception e) {

            // Si los servidores que han dado de alta los servicios en el broker terminan su ejecución (p.e. Ctrl C) o fallan,
            // la ejecución de los servicios fallará, pero seguirán apareciendo en el listado de servicios disponibles. Añadimos
            // el siguiente bloque de código para que si un servicio falla se desuscriba del broker y se elimine del listado.
            if(
                e instanceof java.rmi.ConnectException ||
                e instanceof java.rmi.ConnectIOException ||
                e instanceof java.rmi.UnmarshalException ||
                e instanceof java.rmi.UnknownHostException
            ) {
                ServicioInfo sinfo = servicios.get(nom_servicio);
                if (sinfo != null) {
                    System.err.println("Servidor \"" + sinfo.nom_servidor + "\" no accesible. Eliminando todos sus servicios del Broker.");
                    ArrayList<String> serviciosAsociados = new ArrayList<>();

                    // Buscar todos los servicios del servidor caído
                    for (Map.Entry<String, ServicioInfo> entry : servicios.entrySet()) {
                        if (entry.getValue().nom_servidor.equals(sinfo.nom_servidor))
                            serviciosAsociados.add(entry.getKey());
                    }

                    // Dar de baja cada servicio
                    for (String servicio : serviciosAsociados)
                        baja_servicio(sinfo.nom_servidor, servicio);

                    servidores.remove(sinfo.nom_servidor);
                }
            }

            throw new RemoteException("Error: " + e.getMessage());
        }
    }

    /*
     * Pre:  Dado un nombre de un servicio y los parámetros requeridos para ejecutarlo.
     * Post: El siguiente procedimiento ejecuta el servicio de forma asíncrona.
     */
    @Override
    public void ejecutar_servicio_asinc(String nom_servicio, ArrayList<Object> parametros_servicio)
        throws RemoteException {

        try {
            String client = getClientHost();
            String key = nom_servicio + ":" + client;
            
            if (respuestaEntregada.getOrDefault(key, true)) {
                Future<Object> future = executor.submit(() -> {
                    try {
                        Respuesta<Object> resp = ejecutar_servicio(nom_servicio, parametros_servicio);
                        return resp.getResultado();
                    } catch (RemoteException e) {
                        return "Error en ejecución remota: " + e.getMessage();
                    } catch (Exception e) {
                        return "Error inesperado: " + e.getMessage();
                    }
                });

                resultadosAsinc.put(key, future);
                respuestaEntregada.put(key, false);
            } else {
                System.err.println("Recoja la respuesta anterior del servicio \"" + nom_servicio + "\" antes de volver a ejecutarlo.");
            }
        }
        catch (Exception e) {
            System.err.println("Error al ejecutar servicio asíncrono: " + e.getMessage());
        }
    }

    /*
     * Pre:  Dado una cadena de carácteres "nom_servicio".
     * Post: La siguiente función obtiene el resultado de la llamada asíncrona de "nom_servicio".
     */
    @Override
    public Respuesta<Object> obtener_respuesta_asinc(String nom_servicio)
        throws RemoteException {

        try {
            String client = getClientHost();
            String key = nom_servicio + ":" + client;

            Future<Object> future = resultadosAsinc.get(key);

            if (future == null)
                throw new RemoteException("Error: No hay ejecución asíncrona para \"" + nom_servicio + "\".");

            if (respuestaEntregada.getOrDefault(key, false))
                throw new RemoteException("Error: La respuesta ya fue entregada previamente para " +
                    client + " ,con: " + nom_servicio);

            Object resultado = future.get();
            respuestaEntregada.put(key, true); // Marcar como entregada
            return new Respuesta<>(resultado);

        } catch (Exception e) {
            return new Respuesta<>("Error: " + e.getMessage());
        }
    }

    /*----------------------------------------------------------------------------------*
     * Funciones auxiliares                                                             *
     *----------------------------------------------------------------------------------*/
    private boolean tipoCoincide(Class<?> claseEsperada, String tipoDeclarado, Object parametro) {
        if (parametro == null) {
            return !claseEsperada.isPrimitive(); // null solo es válido para tipos no primitivos
        }

        Class<?> claseParametro = parametro.getClass();

        switch (tipoDeclarado.toLowerCase()) {
            case "int":
                return claseEsperada == int.class || claseEsperada == Integer.class || claseParametro == Integer.class;
            case "double":
                return claseEsperada == double.class || claseEsperada == Double.class || claseParametro == Double.class;
            case "boolean":
                return claseEsperada == boolean.class || claseEsperada == Boolean.class || claseParametro == Boolean.class;
            case "float":
                return claseEsperada == float.class || claseEsperada == Float.class || claseParametro == Float.class;
            case "long":
                return claseEsperada == long.class || claseEsperada == Long.class || claseParametro == Long.class;
            case "short":
                return claseEsperada == short.class || claseEsperada == Short.class || claseParametro == Short.class;
            case "byte":
                return claseEsperada == byte.class || claseEsperada == Byte.class || claseParametro == Byte.class;
            case "char":
                return claseEsperada == char.class || claseEsperada == Character.class || claseParametro == Character.class;
            default:
                return claseEsperada.getSimpleName().equalsIgnoreCase(tipoDeclarado)
                    || claseEsperada.isAssignableFrom(claseParametro);
        }
    }


    /*----------------------------------------------------------------------------------*
     * Main                                                                             *
     *----------------------------------------------------------------------------------*/

    public static void main(String[] args) {

        final String sufijo = "506";

        if(args.length != 2) {
            System.err.println("Uso: java BrokerImpl <ip> <puerto>");
            System.exit(-1);
        }

        // Obtengo parámetros (ip y puerto donde correrá el broker)
        String ip = args[0], puerto = args[1];
        String url = "//" + ip + ":" + puerto + "/Broker" + sufijo;

        // Establecer permisos y política de seguridad
        System.setProperty("java.security.policy", "./java.policy");
        
        // Establecer securityManager. Función obsoleta, la ponemos
        // por qué así sale en los pdfs de las prácticas
        System.setSecurityManager(new SecurityManager());

        try {
            BrokerImpl broker = new BrokerImpl();
            Naming.rebind(url, broker);
            System.out.println("¡Estoy registrado! Broker disponible en " + url);
        }
        catch (Exception e) {
            System.err.println("Error en Broker: " + e.getMessage());
        }
    }
}