//-------------------------------------------------------------------------------------------
// File:   Diccionario.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   18 de abril de 2025
// Coms:   Fichero interfaz de la clase Diccionario, de la práctica 3 de Arquitectura
//         Software. Servidor que proporciona funciones de un diccionario.
//-------------------------------------------------------------------------------------------

package servidores;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Diccionario extends Remote {

    /*
     * Pre : Dado dos cadenas de caracteres "palabra" y "traduccion".
     * Post: El siguiente procedimiento guarda la "palabra" con su respectiva "traduccion" en
     *       el diccionario.
     */
    public void agnadirPalabra(String palabra, String traduccion) throws RemoteException;

    /*
     * Pre : Dado una palabra que se desea conseguir su correspondiente traducción por el
     *       diccionario.
     * Post: Esta función devuelve la traducción de "palabra" registrada en el diccionario.
     *       En caso de que no esté registrado, devuelve '(Desconocido)'.
     */
    public String traducir(String palabra) throws RemoteException;

    /*
     * Pre : Dado el <<String>> "pref".
     * Post: Función que devuelve el número de palabras en el diccionario que empiezan por la
     *       cadena "pref".
     */
    public int numPalabrasConPrefijo(String pref) throws RemoteException;
}