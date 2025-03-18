//-------------------------------------------------------------------------------------------
// File:   Collection.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   18 de marzo de 2025
// Coms:   Fichero java de la clase Collection, de la práctica 3 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.rmi.Remote;
import java.rmi.RemoteException;
// TODO : otros imports

public interface Collection extends Remote {
    // Métodos de la interfaz
    int number_of_books() throws RemoteException;
    String name_of_collection() throws RemoteException;
    void name_of_collection(String _new_value) throws RemoteException;
}