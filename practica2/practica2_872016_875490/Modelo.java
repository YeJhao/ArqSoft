
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Modelo {

    /** Patrón singleton */
    private static Modelo instance;

    public static Modelo getInstance() {
        if(instance == null) {
            instance = new Modelo();
        }
        return instance;
    }

    /** Atributos */
    private final Vector<Class> classes;                     // clases
    private final Vector<Association> associations;          // asociaciones
    private final Map<Aspecto, Set<Observador>> observadores;   // observadores suscritos

    
    /** Constructor privado */
    private Modelo() {
        classes = new Vector<>();
        associations = new Vector<>();
        observadores = new HashMap<>();
    }

    /** Getter classes */
    public Vector<Class> classes() {
        return classes;
    }

    /** Setter classes */
    public Vector<Association> associations() {
        return associations;
    }

    /** Devuelve el número de clases */
    public int getNClasses(){
		return classes.size();
	}

    /** Devuelve el número de asociaciones */
    public int getNAssociations(){
		return associations.size();
	}

    /** Métodos para cambiar el estado */
    public void addClass(Class c) {
        classes.add(c);
        notify(Aspecto.CLASS);
        notify(Aspecto.ALL);
    }

    public void addAssociation(Association a) {
        associations.add(a);
        notify(Aspecto.ASSOCIATION);
        notify(Aspecto.ALL);
    }

    public void removeClass(Class c) {
        if(!c.associations().isEmpty()) {
            associations.removeAll(c.associations());
            c.deleteAssociations();
            notify(Aspecto.ASSOCIATION);
        }
        classes.remove(c);
        notify(Aspecto.CLASS);
        notify(Aspecto.ALL);
    }

    public void moveClass(Class c, int x, int y) {
        c.setPosition(x, y);
        notify(Aspecto.ALL);
    }

    public void selectClass(Class c, boolean selected) {
        c.setSelected(selected);
        notify(Aspecto.ALL);
    }

    public void hoverClass(Class c, boolean candidate) {
        c.setCandidate(candidate);
        notify(Aspecto.ALL);
    }

    /** Suscribir */
    public void attach(Observador obs, Aspecto asp) {
        observadores.putIfAbsent(asp, new HashSet<>());
        observadores.get(asp).add(obs);
    }

    /** Notificar */
    public void notify(Aspecto asp) {
        Set<Observador> obsSet = observadores.get(asp);
        if (obsSet != null) {
            for (Observador obs : obsSet) {
                obs.update();
            }
        }
    }
}