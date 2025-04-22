//-------------------------------------------------------------------------------------------
// File:   Inventario.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   18 de abril de 2025
// Coms:   Fichero interfaz de la clase Inventario, de la práctica 3 de Arquitectura
//         Software. Servidor que proporciona funciones de un inventario.
//-------------------------------------------------------------------------------------------

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Inventario extends Remote {
    
    /*
     * Pre : Dado una cadena de caracteres "nombre", identificando un producto y un entero
     *       "uds", indicando la cantidad del producto.
     * Post: El siguiente procedimiento añade en el inventario al producto, las unidades que
     *       se pasan como parámetro.
     */
    void agnadirProducto(String nombre, int uds) throws RemoteException;

    /*
     * Pre : Dado el nombre de un producto, el cual se desea buscar.
     * Post: Esta función devuelve un entero muestra la cantidad del producto presente en
     *       el inventario.
     */
    int obtenerUnidades(String nombre) throws RemoteException;

    /*
     * Pre : --
     * Post: Función que devuelve la lista de los productos existentes en el inventario.
     */
    ArrayList<String> listarProductos() throws RemoteException;

}