import java.rmi.Naming;
// TODO : imports necesarios
import java.rmi.RMISecurityManager;

public class Cliente {    
    public static void main (String[] args) {
        // TODO : Fijar el directorio donde se encuentra el java.policy
        System.setProperty("java.security.policy", "./java.policy");
        
        if (System.getSecurityManager() == null) {
            // TODO : Crear administrador de seguridad
            System.setSecurityManager(new SecurityManager());
        }
        try {
            /** PASO 1 - Obtener una referencia al objeto servidor creado anteriormente
              * Nombre del host servidor o su IP. Es dónde se buscará al objeto remoto
              */
            String hostname = "127.0.0.1:32000"; // se puede usar "IP: puerto "
            Collection server = (Collection) Naming.lookup("rmi://"+ hostname + "/MyCollection") ;
            
            /** PASO 2 - Invocar remotamente los metodos del objeto servidor : */
            
            /** Obtener el nombre de la colección y número de libros */
            System.out.println("Atributos colección");
            System.out.println("├─ Nombre de la colección: " + server.name_of_collection());
            System.out.println("└─ Número de libros: " + server.number_of_books());
            
            /** Cambiar el nombre de la colección y ver que ha funcionado */
            server.name_of_collection("Nuevo nombre");
            System.out.println("\nAtributos después de actualización");
            System.out.println("├─ Nombre de la colección: " + server.name_of_collection());
            System.out.println("└─ Número de libros: " + server.number_of_books());

        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}