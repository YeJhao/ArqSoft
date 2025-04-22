import java.io.Serializable;
import java.util.ArrayList;

public class ServicioInfo implements Serializable {
    public final String nom_servidor;
    public final String nom_servicio;
    public final ArrayList<String> lista_param;
    public final String tipo_retorno;
    public final String url;
    public final String description;

    /** Constructor */
    public ServicioInfo(String nom_servidor, String nom_servicio,
        ArrayList<String> lista_param, String tipo_retorno, String url, String description) {
        
        this.nom_servidor = nom_servidor;
        this.nom_servicio = nom_servicio;
        this.lista_param = lista_param;
        this.tipo_retorno = tipo_retorno;
        this.url = url;
        this.description = description;
    }

    /**
     * Pre : ---
     * Post: Devuelve la descripción del servicio si existe, o nulo en su defecto.
     */
    public String getDescription() {
        return description;
    }
}