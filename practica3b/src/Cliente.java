//-------------------------------------------------------------------------------------------
// File:   Diccionario.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   18 de abril de 2025
// Coms:   Cliente del broker
//-------------------------------------------------------------------------------------------

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Cliente {

    private static final Scanner scanner = new Scanner(System.in);
    private static Map<String, ServicioInfo> serviciosMap;


    /** 
     * Función para ejecutar un servicio usando el broker. Se puede realizar
     * de manera síncrona o asíncrona
     */
    private static void ejecutarServicio(Broker broker, boolean asincrono) {
        try {
            System.out.print("Nombre del servicio: ");
            String nom_servicio = scanner.nextLine().trim();
            
            if(nom_servicio.isEmpty()) {
                System.out.println("Nombre de servicio vacío.");
                return;
            }

            ServicioInfo info = serviciosMap.get(nom_servicio);
            if (info == null) {
                System.out.println("Servicio no encontrado.");
                return;
            }

            ArrayList<Object> parametros = new ArrayList<>();
            List<String> tipos = info.lista_param;

            // Obtener parámetros
            if (!tipos.isEmpty()) {
                System.out.print("Introduce los parámetros separados por espacio (" + String.join(" ", tipos) + "): ");
                String[] entrada = scanner.nextLine().trim().split(" ");

                if (entrada.length != tipos.size()) {
                    System.out.println("Número incorrecto de parámetros.");
                    return;
                }

                for (int i = 0; i < tipos.size(); i++) {
                    try {
                        parametros.add(parsear(entrada[i], tipos.get(i)));
                    } catch (Exception e) {
                        System.out.println("Error en parámetro " + (i + 1) + ": " + e.getMessage());
                        return;
                    }
                }
            }

            if (asincrono) {
                broker.ejecutar_servicio_asinc(nom_servicio, parametros);
                System.out.println("Servicio ejecutado de forma asíncrona.");
            } else {
                Respuesta<?> respuesta = broker.ejecutar_servicio(nom_servicio, parametros);
                System.out.println("Resultado: " + respuesta);
            }

        } catch (RemoteException e) {
            System.err.println("Error de comunicación RMI: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al ejecutar servicio: " + e.getMessage());
        }
    }

    /** Parsear parámetros */
    private static Object parsear(String valor, String tipo) {
        return switch (tipo.toLowerCase()) {
            case "int" -> Integer.parseInt(valor);
            case "double" -> Double.parseDouble(valor);
            case "float" -> Float.parseFloat(valor);
            case "boolean" -> Boolean.parseBoolean(valor);
            case "long" -> Long.parseLong(valor);
            default -> valor; // Asumimos String por defecto
        };
    }

    /** Programa principal */
    public static void main(String[] args) {
        // Fijar el directorio donde se encuentra el java.policy
        System.setProperty("java.security.policy", "./java.policy");

        if(System.getSecurityManager() == null) {
            // Crear administrador de seguridad
            System.setSecurityManager(new SecurityManager());
        }

        if(args.length != 1) {
            System.err.println("Uso: java Cliente <ip_broker:puerto_broker>");
            System.exit(-1);
        }
        
        final String direccionBroker = args[0];   
        final String sufijo = "506";
        final String url = "rmi://" + direccionBroker + "/Broker" + sufijo;

        try {
            /** 
             * PASO 1 - Obtener una referencia al objeto servidor creado anteriormente
             * Nombre del host servidor o su IP. Es dónde se buscará al objeto remoto
             */

            Broker broker = (Broker) Naming.lookup(url);
            System.out.println("Conectado con el broker: \"" + url + "\".");

            /**
             * PASO 2 - Invocar remotamente los métodos/servicios suscritos en el broker
             */
            serviciosMap = broker.lista_servicios();
            boolean run = true;
            while (run) { 

                System.out.println("\nMenú:");
                System.out.println("   1................Listar servicios");
                System.out.println("   2.........Descripción de servicio");
                System.out.println("   3....Ejecutar servicio (síncrono)");
                System.out.println("   4...Ejecutar servicio (asíncrono)");
                System.out.println("   5.....Obtener respuesta asíncrona");
                System.out.println("   6...........................Salir");

                System.out.print("Introduce una opción: ");
                String entrada = scanner.nextLine().trim();

                int opcion;
                try {
                    opcion = Integer.parseInt(entrada);
                } catch (NumberFormatException e) {
                    System.out.println("Opción no válida. Introduce un número.");
                    continue;
                }

                switch (opcion) {
                    case 1 -> {
                        serviciosMap = broker.lista_servicios();
                        listarServicios();
                    }
                    case 2 -> descripcionServicio();
                    case 3 -> ejecutarServicio(broker, false);
                    case 4 -> ejecutarServicio(broker, true);
                    case 5 -> obtenerRespuestaAsinc(broker);
                    case 6 -> {
                        System.out.println("Saliendo...");
                        run = false;
                    }
                    default -> System.out.println("Opción inválida.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error en cliente: " + e.getMessage());
        }
    }

    /** Listar servicios disponibles */
    private static void listarServicios() {
        if (serviciosMap == null || serviciosMap.isEmpty()) {
            System.out.println("No hay servicios disponibles.");
            return;
        }
        System.out.println("Servicios registrados:");
        serviciosMap.keySet().forEach(s -> System.out.println("- " + s));
    }

    /** Mostrar descripción de un servicio */
    private static void descripcionServicio() {
        System.out.print("Nombre del servicio: ");
        String nombre = scanner.nextLine().trim();
        ServicioInfo info = serviciosMap.get(nombre);

        if (info != null) {
            System.out.println(info.getDescription());
            System.out.println(String.join(" ", info.lista_param) + " -> " + info.tipo_retorno);
        } else {
            System.out.println("Servicio no encontrado.");
        }
    }

    /** Obtener respuesta asíncrona */
    private static void obtenerRespuestaAsinc(Broker broker) {
        try {
            System.out.print("Nombre del servicio: ");
            String nom_servicio = scanner.nextLine().trim();
            Respuesta<?> respuesta = broker.obtener_respuesta_asinc(nom_servicio);
            System.out.println("Respuesta: " + respuesta);
        } catch (RemoteException e) {
            System.err.println("Error de comunicación: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al obtener respuesta: " + e.getMessage());
        }
    }
}