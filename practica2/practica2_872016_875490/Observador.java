//-------------------------------------------------------------------------------------------
// File:   Observador.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   17 de marzo de 2025
// Coms:   Fichero java de la interfaz Observador, de la práctica 2 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

public interface Observador {
    /*
	 * Pre:
	 * Post: Procedimiento que realiza las actualizaciones del estado, según el sujeto que
     *       observa.
	 */
    public void update();
}