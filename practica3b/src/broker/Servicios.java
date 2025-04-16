//-------------------------------------------------------------------------------------------
// File:   Servicios.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   16 de abril de 2025
// Coms:   Fichero java de la clase Servicios, de la práctica 3 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

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

    /*
     * Pre : --
     * Post: Función que devuelve una cadena de carácteres de los servicios registrados.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Servicios registrados:\n");

        // TODO: Shows the params too
        for (String servicio : servicios) {
            sb.append("- ").append(servicio).append("\n");
        }
        
        return sb.toString();
    }
}