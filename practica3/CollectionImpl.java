//-------------------------------------------------------------------------------------------
// File:   CollectionImpl.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   18 de marzo de 2025
// Coms:   Fichero java de la clase CollectionImpl, de la práctica 3 de Arquitectura Software.
//         Implementa una o más interfaces remotas para crear objetos remotos.
//-------------------------------------------------------------------------------------------

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
// TODO : imports necesarios

public class CollectionImpl extends UnicastRemoteObject implements Collection, Serializable {
    // Private member variables
    private final int m_number_of_books;
    private String m_name_of_collection;
    // Constructor
    public CollectionImpl (String nameCollection, int numberBooks) throws RemoteException {
        super () ; // Llama al constructor de UnicastRemoteObject
        // TODO : inicializar las variables privadas
        m_name_of_collection = nameCollection;
        m_number_of_books = numberBooks;
    }

    // TODO : Implementar todos los metodos de la interface remota
    @Override
    public int number_of_books () throws RemoteException {
        return m_number_of_books;
    }

    @Override
    public String name_of_collection() throws RemoteException {
        return m_name_of_collection;
    }

    @Override
    public void name_of_collection(String _new_value) throws RemoteException {
        m_name_of_collection = _new_value;
    }

    public static void main (String args []) {
        // Fijar el directorio donde se encuentra el java.policy
        // El segundo argumento es la ruta al java.policy
        System.setProperty("java.security.policy", "./java.policy");
        // Crear administrador de seguridad
        System.setSecurityManager(new SecurityManager());
        // Nombre o IP del host donde reside el objeto servidor
        String hostName = "127.0.0.1:32000"; // se puede usar "IPhostremoto : puerto "
        // Por defecto , RMI usa el puerto 1099
        try {
            // Crear objeto remoto
            CollectionImpl obj = new CollectionImpl("Default", 0);
            System.out.println(" Creado !");
            // Registrar el objeto remoto
            Naming.rebind("//" + hostName + "/MyCollection", obj);
            System.out.println(" Estoy registrado !");
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }
}