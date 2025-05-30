//-------------------------------------------------------------------------------------------
// File:   Cola.java
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   19 de mayo de 2025
// Coms:   Fichero java de la clase Cola, de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Cola implements Serializable {

    /** Nombre identificativo de la cola */
    private final String nombre;

    /** Mensajes en la cola */
    private Queue<Mensaje> mensajes = new LinkedList<>();

    /** 
     * Mensajes pendientes de confirmación
     * Versión avanzada: Reconocimiento (ACK)
     */
    private Map<String, Long> pendientes = new HashMap<>();

    private transient Map<String, Consumidor> consumidores = new ConcurrentHashMap<>();
    private transient Set<String> ocupados = ConcurrentHashMap.newKeySet();

    /** 
     * Tiempo que puede durar un mensaje en la cola sin consumidores
     * disponibles en milisegundos
     */
    private static final long TIMEOUT_NO_CONSUMIDOR = 5 * 60 * 1000;

    /** 
     * Tiempo sin recibir ACK antes de reenvío
     * Versión avanzada: Reconocimiento (ACK)
     */
    private static final long TIMEOUT_ACK = 2 * 60 * 1000;


    /****************************************
     * Métodos y procedimientos             *
     ****************************************/

    public Cola(String nombre) {
        this.nombre = nombre;
        cargar();
    }

    /**
     * Pre:  ---
     * Post: Devuelve el nombre de la cola.
     */
    public String getNombre() { return nombre; }

    /**
     * Pre:  "msg" no es null.
     * Post: Añade el mensaje "msg" a la cola con una marca de tiempo y 
     *       lo guarda en disco (Versión avanzada: durability).
     */
    public void encolar(String msg) {
        mensajes.offer(new Mensaje(msg, System.currentTimeMillis()));
        guardar();
    }

    /**
     * Pre:  ---
     * Post: Entrega un mensaje al consumidor si no está ocupado. Si está
     *       disponible marca el mensaje como pendiente de ack. Si no hay
     *       consumidores disponibles durante 5 minutos descarta el mensaje.
     */
    public String entregarMensaje(Consumidor consumidor) {
        try {
            if(mensajes.isEmpty() || ocupados.contains(consumidor.getId()))
                return null;

            Mensaje msg = mensajes.peek();
            if(msg == null) return null;

            if(!tieneConsumidores()) {
                if(System.currentTimeMillis() - msg.getTime() > TIMEOUT_NO_CONSUMIDOR) {
                    mensajes.poll();
                    guardar();
                }
                return null;
            }

            mensajes.poll();
            pendientes.put(msg.getContent(), System.currentTimeMillis());
            ocupados.add(consumidor.getId());
            guardar();
            return msg.getContent();
        } catch(RemoteException e) {
            return null;
        }
    }

    /**
     * Pre:  --- 
     * Post: Elimina el mensaje de pendientes y libera al consumidor para
     *       que pueda procesar nuevos mensajes.
     */
    public boolean  confirmarACK(String mensaje, String consumidorId) {
        if(pendientes.remove(mensaje) != null) {
            ocupados.remove(consumidorId);
            guardar();
            return true;
        }
        return false;
    }

    /**
     * Pre:  ---
     * Post: Recoloca en la cola todos los mensajes pendientes que han
     *       expirado tras 2 minutos sin ACK.
     */
    public List<String> recuperarMensajesExpirados() {
        long ahora = System.currentTimeMillis();
        List<String> reenviar = new ArrayList<>();

        for(Map.Entry<String, Long> entry : pendientes.entrySet()) {
            if(ahora - entry.getValue() > TIMEOUT_ACK) {
                reenviar.add(entry.getKey());
            }
        }

        for(String msg : reenviar) {
            pendientes.remove(msg);
            mensajes.offer(new Mensaje(msg, System.currentTimeMillis()));
        }

        if(!reenviar.isEmpty()) guardar();
        return reenviar;
    }

    /**
     * Pre:  Consumidor "c" no es null.
     * Post: Añade el consumidor a la lista de consumidores suscritos a esa cola.
     */
    public void registrarConsumidor(Consumidor c) {
        try {
        consumidores.put(c.getId(), c);
        } catch(RemoteException e) {}
    }

    /**
     * Pre:  ---
     * Post: Devuelve true si y solo si hay al menos un consumidor registrado.
     */
    public boolean tieneConsumidores() {
        return !consumidores.isEmpty();
    }

    /**
     * Pre:  ---
     * Post: Devuelve una lista con los consumidores actualmente disponibles,
     *       los no ocupados procesando ya algún mensaje previo.
     */
    public List<Consumidor> getConsumidoresDisponibles() {
        List<Consumidor> disponibles = new ArrayList<>();
        for(Map.Entry<String, Consumidor> entry : consumidores.entrySet()) {
            if(!ocupados.contains(entry.getKey())) {
                disponibles.add(entry.getValue());
            }
        }
        System.out.println("Disponibles: " + disponibles.size());
        return disponibles;
    }

    /**
     * Pre:  ---
     * Post: Devuelve todos los consumidores suscritos a la cola de mensajes
     */
    public List<Consumidor> getConsumidores() {
        return new ArrayList<>(consumidores.values());
    }

    /****************************************
     * Métodos privados                     *
     ****************************************/

    /**
     * Pre:
     * Post: Devuelve una cadena de caracteres, con la ruta en la que se almacena la cola.
     */
    private String getRuta() {
        return "data/cola_" + nombre + ".dat";
    }

    /**
     * Pre:
     * Post: Procedimiento que guarda los datos de la cola de forma persistente (fichero).
     */
    private void guardar() {
        try(ObjectOutputStream out = new ObjectOutputStream(
            new FileOutputStream(getRuta())
        )) {
            out.writeObject(new LinkedList<>(mensajes));
            out.writeObject(new HashMap<>(pendientes));
        } catch(IOException e) {
            System.err.println("Error guardando cola \"" + nombre + "\": " + e.getMessage());
        }
    }

    /**
     * Pre:
     * Post: El siguiente procedimiento saca los datos de la cola de memoria (fichero).
     */
    private void cargar() {
        File archivo = new File(getRuta());
        if(!archivo.exists()) {
            return;
        }
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            mensajes = (Queue<Mensaje>) in.readObject();
            pendientes = (Map<String, Long>) in.readObject();
        } catch(IOException | ClassNotFoundException e) {
            System.err.println("Error cargando cola \"" + nombre + "\": " + e.getMessage());
        }
        ocupados = ConcurrentHashMap.newKeySet();
        consumidores = new ConcurrentHashMap<>();
    }

    /****************************************
     * Clase auxiliar Mensaje               *
     ****************************************/
    
    private static class Mensaje implements Serializable {
        private final String content;
        private final long time;

        /** Constructor */
        public Mensaje(String content, long time) {
            this.content = content;
            this.time = time;
        }

        public String getContent() { return content; }

        public long getTime() { return time; }
    }
}