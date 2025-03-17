//-------------------------------------------------------------------------------------------
// File:   Modelo.java
// Author: Jorge Soria Romero (872016) y Jiahao Ye (875490)
// Date:   17 de marzo de 2025
// Coms:   Fichero java de la clase Modelo, de la práctica 2 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

// Clase Modelo
public class Modelo {

    /** Patrón Singleton */
    private static Modelo instance;

	/*
	 * Pre:
	 * Post: Función que devuelve la instancia del modelo.
	 */
    public static Modelo getInstance() {
        if(instance == null) {
            instance = new Modelo();
        }
        return instance;
    }

    /** Atributos */
    private final Vector<Class> classes;                        // Clases
    private final Vector<Association> associations;             // Asociaciones
    private final Map<Aspecto, Set<Observador>> observadores;   // Observadores suscritos

    
    /** Constructor privado */
    private Modelo() {
        classes = new Vector<>();
        associations = new Vector<>();
        observadores = new HashMap<>();
    }

	/*
	 * Pre:
	 * Post: Función que devuelve las clases.
	 */
    public Vector<Class> classes() {
        return classes;
    }

	/*
	 * Pre:
	 * Post: Función que devuelve las asociaciones.
	 */
    public Vector<Association> associations() {
        return associations;
    }

	/*
	 * Pre:
	 * Post: Función que devuelve el número de clases almacenadas.
	 */
    public int getNClasses(){
		return classes.size();
	}

	/*
	 * Pre:
	 * Post: Función que devuelve el número de asociaciones almacenadas.
	 */
    public int getNAssociations(){
		return associations.size();
	}

	/*
	 * Pre:  Dado una clase "c".
	 * Post: Procedimiento que añade la clase "c" al modelo.
	 */
    public void addClass(Class c) {
        classes.add(c);

        notify(Aspecto.CLASS);
        notify(Aspecto.ALL);
    }

    /*
	 * Pre:  Dado una asociación "a".
	 * Post: Procedimiento que añade la asociación "a" al modelo.
	 */
    public void addAssociation(Association a) {
        associations.add(a);

        notify(Aspecto.ASSOCIATION);
        notify(Aspecto.ALL);
    }

	/*
	 * Pre:  Dado una clase "c".
	 * Post: Procedimiento que borra la clase "c" al modelo.
	 */
    public void removeClass(Class c) {
        if (!c.associations().isEmpty()) {
            associations.removeAll(c.associations());
            c.deleteAssociations();
            notify(Aspecto.ASSOCIATION);
        }
        classes.remove(c);

        notify(Aspecto.CLASS);
        notify(Aspecto.ALL);
    }

	/*
	 * Pre:  Dado una clase "c" y unas coordenadas "x" e "y".
	 * Post: Procedimiento que mueve "c" a las coordenadas designadas.
	 */
    public void moveClass(Class c, int x, int y) {
        c.setPosition(x, y);

        notify(Aspecto.ALL);
    }

    /*
	 * Pre:  Dado una clase "c" y un booleano "selected".
	 * Post: Procedimiento que redefine el estado de selección de la clase.
	 */
    public void selectClass(Class c, boolean selected) {
        c.setSelected(selected);

        notify(Aspecto.ALL);
    }

    /*
	 * Pre:  Dado una clase "c" y un booleano "candidate".
	 * Post: Procedimiento que redefine el estado de hover de la clase.
	 */
    public void hoverClass(Class c, boolean candidate) {
        c.setCandidate(candidate);
        notify(Aspecto.ALL);
    }

    /*
	 * Pre:  Dado un observador y un aspecto de interés.
	 * Post: El siguiente procedimiento suscribe el observador al modelo, con el aspecto "asp".
	 */
    public void attach(Observador obs, Aspecto asp) {
        observadores.putIfAbsent(asp, new HashSet<>());
        observadores.get(asp).add(obs);
    }

    /*
	 * Pre:  Dado aspecto de interés.
	 * Post: Procedimiento que notifica de la actualización, en relación a "asp".
	 */
    public void notify(Aspecto asp) {
        Set<Observador> obsSet = observadores.get(asp);
        
        if (obsSet != null) {
            for (Observador obs : obsSet) {
                obs.update();
            }
        }
    }
}