//-------------------------------------------------------------------------------------------
// File:   Broker.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   17 de abril de 2025
// Coms:   Fichero java que sirve como cliente para la práctica 3 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

// TODO: ¿Se podría mejorar la distribución de las clases?
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import broker.BrokerImpl;
import broker.Respuesta;
import broker.Servicios;
import broker.BrokerImpl.ServicioInfo;
import java.util.*;

public class Cliente {    
    public static void main (String[] args) {
        // Fijar el directorio donde se encuentra el java.policy
        System.setProperty("java.security.policy", "./java.policy");
        
        if (System.getSecurityManager() == null) {
            // Crear administrador de seguridad
            System.setSecurityManager(new SecurityManager());
        }
        try {
            /** PASO 1 - Obtener una referencia al objeto servidor creado anteriormente
              * Nombre del host servidor o su IP. Es dónde se buscará al objeto remoto
              */

            // TODO: Solo se puede hardcorear la dirección?
            String hostname = "127.0.0.1:32000"; // se puede usar "IP: puerto "
            BrokerImpl broker = (BrokerImpl) Naming.lookup("rmi://"+ hostname + "/BrokerImpl") ;
            
            /** PASO 2 - Invocar remotamente los metodos del objeto servidor : */

            Scanner scanner = new Scanner(System.in);

            boolean run = true;

            while (run) {
                System.out.println("Servicios disponibles:");
                
                Servicios servicios = broker.lista_servicios();

                System.out.println(servicios.toString());
                System.out.println("- obtener_respuesta_asinc");
                System.out.println("- fin");

                System.out.println();

                System.out.println("Forma de ejecución: <nombre_servicio> [ -s | -a | -h ] <lista_params>");
                System.out.println("    -s : Ejecución síncrona");
                System.out.println("    -a : Ejecución asíncrona");
                System.out.println("    -h : Descripción del servicio");
                System.out.println("    <lista_params> : Lista de parámetros separados por espacios (puede estar vacío)");

                // Esperar entrada del usuario
                System.out.print("Ingrese el comando: ");

                // Obtenemos la cadena introducida
                String input = scanner.nextLine();

                // Troceamos la entrada de usuario
                String[] input_partes = input.trim().split("\\s+");

                // Si desea terminar ejecución
                if (input_partes[0].equals("fin")) {
                    run = false;
                    continue;
                }
                
                System.out.println("----------------------------------");

                if (input_partes[0].equals("obtener_respuesta_asinc")) {
                    // Mostrar resultado de ejecución asíncrona
                    String nom_servicio = input_partes[input_partes.length - 1];

                    Respuesta<Object> res = broker.obtener_respuesta_asinc(nom_servicio);

                    System.out.println("Resultado ejecución " + nom_servicio + " : " + res.toString());
                }

                // Comprobamos la entrada
                if (input_partes.length < 2) {
                    System.out.println("Comando no válido. Formato esperado: <nombre_servicio> [ -s | -a | -h ] <lista_params>");
                    continue;
                }

                ArrayList<Object> parametros = new ArrayList<>(Arrays.asList(input_partes).subList(2, input_partes.length));

                if (input_partes[1].equals("-s")) { // Ejecución síncrona
                    Respuesta<Object> res = broker.ejecutar_servicio(input_partes[0], parametros);

                    System.out.println("Resultado ejecución " + input_partes[0] + " : " + res.toString());
                }
                else if (input_partes[1].equals("-a")) {    // Ejecución asíncrona
                    broker.ejecutar_servicio_asinc(input_partes[0], parametros);
                }
                else if (input_partes[1].equals("-h")) {
                    // Obtener la descripción de nom_servicio
                    ServicioInfo info = broker.getServicioInfo(input_partes[0]);

                    System.out.println(info.getDescription());
                }
                else {
                    System.out.println("Comando no válido. Formato esperado: <nombre_servicio> [ -s | -a | -h ] <lista_params>");
                    continue;
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}