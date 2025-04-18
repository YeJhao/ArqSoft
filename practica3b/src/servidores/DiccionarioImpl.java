//-------------------------------------------------------------------------------------------
// File:   DiccionarioImpl.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   18 de abril de 2025
// Coms:   Fichero implementación de la clase Diccionario, de la práctica 3 de Arquitectura
//         Software. Servidor que proporciona funciones de un diccionario.
//-------------------------------------------------------------------------------------------

package servidores;

import broker.Broker;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiccionarioImpl extends UnicastRemoteObject implements Diccionario {

    private final Map<String, String> diccionario;

    public DiccionarioImpl() throws RemoteException {
        super();
        diccionario = new HashMap<>();
    }

    /*
     * Pre : Dado dos cadenas de caracteres "palabra" y "traduccion".
     * Post: El siguiente procedimiento guarda la "palabra" con su respectiva "traduccion" en
     *       el diccionario.
     */
    @Override
    public void agnadirPalabra(String palabra, String traduccion) throws RemoteException {
        diccionario.put(palabra, traduccion);
    }

    /*
     * Pre : Dado una palabra que se desea conseguir su correspondiente traducción por el
     *       diccionario.
     * Post: Esta función devuelve la traducción de "palabra" registrada en el diccionario.
     *       En caso de que no esté registrado, devuelve '(Desconocido)'.
     */
    @Override
    public String traducir(String palabra) throws RemoteException {
        return diccionario.getOrDefault(palabra, "(Desconocido)");
    }

    /*
     * Pre : Dado el <<String>> "pref".
     * Post: Función que devuelve el número de palabras en el diccionario que empiezan por la
     *       cadena "pref".
     */
    @Override
    public int numPalabrasConPrefijo(String pref) throws RemoteException {
        int count = 0;
        for(String palabra : diccionario.keySet()) {
            if(palabra.startsWith(pref)) count++;
        }
        return count;
    }

    /** Programa principal */
    @SuppressWarnings("UseSpecificCatch")
    public static void main(String[] args) {
        if(args.length != 2) {
            System.err.println("Uso: java DiccionarioImpl <ip_serv:puerto_serv> <ip_broker:puerto_broker>");
            System.exit(-1);
        }

        // Obtengo ips y puertos del servidor y el broker central
        String ip_puerto_servidor = args[0], ip_puerto_broker = args[1];
        String url = "//" + ip_puerto_servidor + "/Diccionario";

        // Establecer permisos y política de seguridad
        System.setProperty("java.security.policy", "./java.policy");
        
        // Establecer securityManager. Función obsoleta, la ponemos
        // por qué así sale en los pdfs de las prácticas
        System.setSecurityManager(new SecurityManager());

        try {
            DiccionarioImpl obj = new DiccionarioImpl();
            Naming.rebind(url, obj);
            System.out.println("Diccionario registrado en: " + url);

            // Registrar servidor en el broker
            Broker broker = (Broker) Naming.lookup("//" + ip_puerto_broker + "/Broker");
            broker.registrar_servidor("DiccionarioServer", url);

            // Dar de alta en el broker los servicios que provee el servidor
            ArrayList<String> p1 = new ArrayList<>(); p1.add("String"); p1.add("String");
            ArrayList<String> p2 = new ArrayList<>(); p2.add("String");

            broker.alta_servicio("DiccionarioServer", "agnadirPalabra", p1, "void",
                                 "El siguiente procedimiento guarda la \"palabra\" con su respectiva \"traduccion\" en el diccionario");
            broker.alta_servicio("DiccionarioServer", "traducir", p2, "String",
                                 "Función que devuelve la traducción del parámetro pasado");
            broker.alta_servicio("DiccionarioServer", "numPalabrasConPrefijo", p2, "int",
                                 "Función que devuelve el número de palabras que comienzan por \"pref\"");
        
        }
        catch (Exception e) {
            System.err.println("Error en Broker: " + e.getMessage());
        }
    }

}