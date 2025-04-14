package servidores;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Inventario extends Remote {
    
    /**
     * Registra 'uds' unidades del producto identificado por 'nombre'  
     * Si el producto ya estaba, se suman las unidades.
     */
    void agnadirProducto(String nombre, int uds) throws RemoteException;

    /** Obtiene el número de unidades que hay del producto 'name' */
    int obtenerUnidades(String name) throws RemoteException;

    /** Devuelve el nombre de todos los productos en el inventario */
    ArrayList<String> listarProductos() throws RemoteException;

}