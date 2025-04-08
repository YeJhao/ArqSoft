import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.Serializable;
import java.util.HashMap;
import java.rmi.Naming;
import java.util.Map;

public class PeliculasImpl extends UnicastRemoteObject implements Peliculas, Serializable {

    /** Clase película */
    private class Pelicula {
        private String nombre, descripcion;
        private float precio;
        private int votos;

        public Pelicula(String nombre, String descripcion, float precio,
                int votos) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.precio = precio;
            this.votos = votos;
        }

        public String get_descripcion() {
            return nombre + " (" + Float.toString(precio) + "):\n" + descripcion;
        }

        public Integer agnadir_voto() { ++votos; return votos; }

        public Float get_precio() { return precio; }
    }

    /** Colección de películas */
    private Map<String, Pelicula> peliculas;

    public PeliculasImpl() throws RemoteException {
        super();
        peliculas = new HashMap<>();
    }

    /**
     * Devuelve el número de películas registradas
     */
    public Integer numero_peliculas() throws RemoteException {
        return peliculas.size();
    }

    /**
     * Devuelve un listado de películas con un precio igual o inferior
     * a 'precio'
     */
    public String filtrar_por_precio(float precio) throws RemoteException {
        StringBuilder resultado = new StringBuilder();
        for (Pelicula p : peliculas.values()) {
            if (p.get_precio() <= precio) {
                resultado.append(p.get_descripcion()).append("\n");
            }
        }
        return resultado.length() > 0 ? resultado.toString() :
            "No hay películas en ese rango de precio.";
    }

    /**
     * Devuelve un listado de n películas ordenadas por votos
     */
    public String mas_votadas(int n) throws RemoteException {
        String res = "";
        for(Pelicula p : peliculas.values()) {
            if(p.get_precio() <= n) {
                res += p.get_descripcion();
            }
        }
        return res.length() > 0 ? res : "No hay películas";
    }

    /**
     * Añade un voto a la película de nombre 'nomPelicula'. Devuelve
     * el número de votos resultante, si no está registrada devuelve -1.
     */
    public Integer agnadir_voto(String nomPelicula) throws RemoteException {
        if(!peliculas.containsKey(nomPelicula))
            return -1;
        return peliculas.get(nomPelicula).agnadir_voto(); 
    }

    /**
     * Devuelve la descripción de una película. Si no existe, un
     * objeto nulo
     */
    public String descripcion_pelicula(String nomPelicula) throws RemoteException {
        if(!peliculas.containsKey(nomPelicula))
            return null;
        return peliculas.get(nomPelicula).get_descripcion();
    }

    /**
     * Execute
     */
    public void execute() {
        //TO DO
    }
}