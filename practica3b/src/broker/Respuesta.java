package broker;

import java.io.Serializable;

/**
 * Clase que representa la respuesta de un servicio
 */
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