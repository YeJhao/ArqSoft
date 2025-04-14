package broker;

import java.io.Serializable;
import java.util.Set;

public class Servicios implements Serializable {
    
    /** Array de nombres de servicios registrados */
    private final Set<String> servicios;

    /** Constructor */
    public Servicios(Set<String> servicios) {
        this.servicios = servicios;
    }

    /**
     * Pre : --
     * Post: Devuelve array de nombre de servicios registrados
     */
    public Set<String> getServicios() {
        return servicios;
    }
}