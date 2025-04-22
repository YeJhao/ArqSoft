//-------------------------------------------------------------------------------------------
// File:   InventarioImpl.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   18 de abril de 2025
// Coms:   Fichero implementación de la clase Inventario, de la práctica 3 de Arquitectura
//         Software. Servidor que proporciona funciones de un inventario.
//-------------------------------------------------------------------------------------------

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InventarioImpl extends UnicastRemoteObject implements Inventario {

    private final Map<String, Integer> inventario;

    /** Constructor */
    public InventarioImpl() throws RemoteException {
        super();
        inventario = new HashMap<>();
    }

    /*
     * Pre : Dado una cadena de caracteres "nombre", identificando un producto y un entero
     *       "uds", indicando la cantidad del producto.
     * Post: El siguiente procedimiento añade en el inventario al producto, las unidades que
     *       se pasan como parámetro.
     */
    @Override
    public void agnadirProducto(String nombre, int uds) throws RemoteException {
        if(nombre == null || nombre.trim().isEmpty())
            throw new RemoteException("El nombre de un producto no puede ser vacío o nulo.");
        
        if(uds <= 0)
            throw new RemoteException("Las unidades de un producto deben ser mayor que cero.");

        inventario.put(nombre, inventario.getOrDefault(nombre, 0) + uds);
    }

    /*
     * Pre : Dado el nombre de un producto, el cual se desea buscar.
     * Post: Esta función devuelve un entero muestra la cantidad del producto presente en
     *       el inventario.
     */
    @Override
    public int obtenerUnidades(String nombre) throws RemoteException {
        return inventario.getOrDefault(nombre, 0);
    }

    /*
     * Pre : --
     * Post: Función que devuelve la lista de los productos existentes en el inventario.
     */
    @Override
    public ArrayList<String> listarProductos() throws RemoteException {
        return new ArrayList<>(inventario.keySet());
    }

    /** Programa principal */
    public static void main(String[] args) {

        final String sufijo = "506";

        if(args.length != 2) {
            System.err.println("Uso: java InventarioImpl <ip_serv:puerto_serv> <ip_broker:puerto_broker>");
            System.exit(-1);
        }

        // Obtengo ips y puertos del servidor y el broker central
        String ip_puerto_servidor = args[0], ip_puerto_broker = args[1];
        String url = "//" + ip_puerto_servidor + "/Inventario" + sufijo;
        
        // Establecer permisos y política de seguridad
        System.setProperty("java.security.policy", "./java.policy");
        
        // Establecer securityManager. Función obsoleta, la ponemos
        // por qué así sale en los pdfs de las prácticas
        System.setSecurityManager(new SecurityManager());

        try {
            InventarioImpl obj = new InventarioImpl();
            Naming.rebind(url, obj);
            System.out.println("Inventario registrado en: " + url);

            // Registrar servidor en el broker
            Broker broker = (Broker) Naming.lookup("//" + ip_puerto_broker + "/Broker" + sufijo);
            broker.registrar_servidor("Inventario", url);
            
            // Dar de alta en el broker los servicios que provee el servidor
            ArrayList<String> p1 = new ArrayList<>(); p1.add("String"); p1.add("int");
            ArrayList<String> p2 = new ArrayList<>(); p2.add("String");
            ArrayList<String> p3 = new ArrayList<>();

            broker.alta_servicio("Inventario", "agnadirProducto", p1, "void",
                                 "Procedimiento que suma la cantidad pasada al producto respectivo");
            broker.alta_servicio("Inventario", "obtenerUnidades", p2, "int",
                                 "Función que devuelve la cantidad del producto que se busca");
            broker.alta_servicio("Inventario", "listarProductos", p3, "ArrayList<String>",
                                 "Función que devuelve la lista de productos existente en el inventario");

        }
        catch (Exception e) {
            System.err.println("Error en Broker: " + e.getMessage());
        }
    }

}