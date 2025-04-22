//-------------------------------------------------------------------------------------------
// File:   Respuesta.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   18 de abril de 2025
// Coms:   Fichero java de la clase Respuesta, de la práctica 3 de Arquitectura Software, que
//         representa el resultado de un servicio.
//-------------------------------------------------------------------------------------------

import java.io.Serializable;

public class Respuesta<T> implements Serializable {
    
    /** Resultado del servicio */
    private final T resultado;    

    /** Constructor */
    public Respuesta(T resultado) {
        this.resultado = resultado;
    }

    /**
     * Pre : ---
     * Post: Devuelve el resultado de la ejecución del servicio
     */
    public T getResultado() {
        return resultado;
    }

    /**
     * Pre : ---
     * Post: Devuelve el resultado en forma de cadena de texto
     */
    @Override
    public String toString() {
        return (resultado != null) ? resultado.toString() : "null";
    }
}