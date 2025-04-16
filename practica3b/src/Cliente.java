//-------------------------------------------------------------------------------------------
// File:   Broker.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   15 de abril de 2025
// Coms:   Fichero java que sirve como cliente para la práctica 3 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import broker.BrokerImpl;
import broker.Servicios;
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
            // TODO: Programa interactivo por terminal, muestra en pantalla los servicios.
            //       Ejecutar los servicios que escriba el usuario en pantalla y terminar cuando desee

            Scanner scanner = new Scanner(System.in);

            boolean run = true;

            while (run) {
                System.out.println("Servicios disponibles:");
                
                Servicios servicios = broker.lista_servicios();

                // TODO: Aún es necesario mirar mejor Servicios
                // TODO: Podría quedar bien, alguna explicación de los servicios, tipo descripción en ServicioInfo
                System.out.println(servicios.toString());
                System.out.println("- fin");

                System.out.println();

                System.out.println("Forma de ejecución: <nombre_servicio> [ -s | -a ] <lista_params>");
                System.out.println("    -s : Ejecución síncrona");
                System.out.println("    -a : Ejecución asíncrona");
                System.out.println("    <lista_params> : Lista de parámetros separados por espacios (puede estar vacío)");

                // TODO: Esperar entrada del usuario
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

                // Comprobamos la entrada
                if (input_partes.length < 2) {
                    System.out.println("Comando no válido. Formato esperado: <nombre_servicio> [ -s | -a ] <lista_params>");
                    continue;
                }

                ArrayList<Object> parametros = new ArrayList<>(Arrays.asList(input_partes).subList(2, input_partes.length));

                if (input_partes[1].equals("-s")) {
                    broker.ejecutar_servicio(input_partes[0], parametros);
                }
                else if (input_partes[1].equals("-a")) {
                    broker.ejecutar_servicio_asinc(input_partes[0], parametros);
                }
                else {
                    System.out.println("Comando no válido. Formato esperado: <nombre_servicio> [ -s | -a ] <lista_params>");
                    continue;
                }
                // TODO: devolver resultado si necesario
                // Si es asíncrono, podríamos poner otra opción en la terminal para devolver la respuesta
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}