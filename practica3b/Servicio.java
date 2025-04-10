
import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Servicio otro = (Servicio) obj;
        return Objects.equals(nom_servicio, otro.nom_servicio) &&
            Objects.equals(nombre_servidor, otro.nombre_servidor) &&
            Objects.equals(lista_param, otro.lista_param) &&
            Objects.equals(tipo_retorno, otro.tipo_retorno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom_servicio, nombre_servidor, lista_param, tipo_retorno);
    }
}