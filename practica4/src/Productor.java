//-------------------------------------------------------------------------------------------
// File:   Productor.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   6 de mayo de 2025
// Coms:   Fichero Productor.java, de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.util.Scanner;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Clase Productor
 */
public class Productor {

    /** Programa principal: productor interactivo por terminal */
    public static void main(String[] args) {
        System.setProperty("java.security.policy", "./java.policy");
        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        if(args.length < 1) {
            System.out.println("Uso: java Productor <host:puerto>");
            return;
        }

        String dir = args[0];       // Host:puerto broker mensajes
        Scanner sc = new Scanner(System.in);

        try {
            // Conexión con broker de mensajes remoto
            Broker broker = (Broker) Naming.lookup("rmi://" + dir + "/Broker");
            System.out.println("Conectando a broker en " + dir);

            String currentQueue = null;
            
            while(true) {
                System.out.println("\nMENÚ:");
                System.out.println("1. Listar colas");
                System.out.println("2. Cambiar cola activa o crear");
                System.out.println("3. Enviar mensaje a cola activa");
                System.out.println("4. Salir");
                System.out.print("\nOpción: ");
                String opcion = sc.nextLine().trim();

                switch(opcion) {
                    case "1":
                        var colas = broker.listar_colas();
                        if(colas.isEmpty()) {
                            System.out.println("No hay colas disponibles.");
                        } else {
                            System.out.println("Colas disponibles:");
                            for(String c : colas) {
                                System.out.println("- " + c);
                            }
                        }
                        break;
                    case "2":
                        System.out.print("Nombre de la nueva cola activa: ");
                        currentQueue = sc.nextLine().trim();
                        try {
                            broker.declarar_cola(currentQueue);
                            System.out.println("Cola \"" + currentQueue + "\" declarada.");
                        } catch (RemoteException e) {
                            System.out.println("La cola \"" +  currentQueue + "\" ya existe. Usándola.");
                        }
                        break;
                    case "3":
                        if(currentQueue == null) {
                            System.out.println("Primero selecciona una cola.");
                            break;
                        }
                        System.out.print("Mensaje a enviar: ");
                        String mensaje = sc.nextLine().trim();
                        try {
                            broker.publicar(currentQueue, mensaje);
                            System.out.println("Mensaje enviado a \"" + currentQueue + "\": " + mensaje);
                        } catch (RemoteException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case "4":
                        System.out.println("Saliendo..."); return;
                    
                    default:
                        System.out.println("Opción inválida."); break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error en Productor: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}