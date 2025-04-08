import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Peliculas extends Remote, Servidor {
    String nombre;
    String IP;
    /**
     * Devuelve el número de películas registradas
     */
    Integer numero_peliculas()
        throws RemoteException;

    /**
     * Devuelve un listado de películas con un precio igual o inferior
     * a 'precio'
     */
    String filtrar_por_precio(float precio)
        throws RemoteException;

    /**
     * Devuelve un listado de n películas ordenadas por votos
     */
    String mas_votadas(int n)
        throws RemoteException;

    /**
     * Añade un voto a la película de nombre 'nomPelicula'. Devuelve
     * el número de votos resultante, si no está registrada devuelve -1.
     */
    Integer agnadir_voto(String nomPelicula)
        throws RemoteException;

    /**
     * Devuelve la descripción de una película. Si no existe, un
     * objeto nulo
     */
    String descripcion_pelicula(String nomPelicula)
        throws RemoteException;
}