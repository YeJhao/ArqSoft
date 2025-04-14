package servidores;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Diccionario extends Remote {

    /** Guarda una palabra y su traducción en el diccionario */
    void agnadirPalabra(String palabra, String traduccion) throws RemoteException;

    /** Devuelve la traducción de 'palabra' si está registrada en el diccionario */
    String traducir(String palabra) throws RemoteException;

    /** Devuelve el número de palabras en el diccionario que empiezan por 'pref' */
    int numPalabrasConPrefijo(String pref) throws RemoteException;

}