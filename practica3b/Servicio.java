
import java.util.ArrayList;

public class Servicio {
    public String nom_servicio;
    public String nombre_servidor;
    public ArrayList<String> lista_param;
    public String tipo_retorno;

    public Servicio(String nom_servicio, String nombre_servidor,
        ArrayList<String> lista_param, String tipo_retorno) {
            this.nom_servicio = nom_servicio;
            this.nombre_servidor = nombre_servidor;
            this.lista_param = lista_param;
            this.tipo_retorno = tipo_retorno;
    }

    @Override
    public String toString() {
        String info = nom_servicio +
            "\n└─" + tipo_retorno;
        return info;
    }
}