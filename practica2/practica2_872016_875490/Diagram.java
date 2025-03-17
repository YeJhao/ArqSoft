//-------------------------------------------------------------------------------------------
// File:   Diagram.java
// Author: Jorge Soria Romero (872016) y Jiahao Ye (875490)
// Date:   17 de marzo de 2025
// Coms:   Fichero java de la clase Diagram, de la práctica 2 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

// Clase Diagram que utiliza el patrón Observer y gestiona entradas del usuario
public class Diagram 
		extends JPanel 
		implements 
			MouseMotionListener, 
			MouseListener,
			KeyListener,
			Observador {
	
	/** Atributos */
	private final Modelo modelo;	// Modelo
	public Class clase; 			// Clase seleccionada
	public Class lastHover;			// Clase que pasa por encima el ratón

	private boolean dragging;		// Booleano que indica true si está arrastrando con el ratón
	private boolean associating;	// Booleano que indica true si se está asociando dos clases
	private int lastX, lastY;		// Últimas coordenadas del ratón

	private final Vector<Class> classes;			// Clases del diagrama
	private final Vector<Association> associations;	// Asociaciones del diagrama

	/** Constructor */
	public Diagram(Modelo modelo) {	
		setBorder(BorderFactory.createLineBorder(Color.black));

		this.modelo = modelo;
		dragging = false;
		associating = false;
		classes = modelo.classes();
		associations = modelo.associations();
	}

	/*
	 * Pre:
	 * Post: Procedimiento que realiza operaciones necesarias para iniciar el diagrama.
	 */
	public void init() {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		modelo.attach(this, Aspecto.ALL);
	}
	
	/*
	 * Pre:  Dado dos enteros "x" e "y", que representan dos coordenadas.
	 * Post: Este procedimiento añade una clase sobre el diagrama.
	 */
	public void addClass(int x, int y) {
		modelo.addClass(new Class(x, y));
	}

	/*
	 * Pre:  Dado una clase "c".
	 * Post: Procedimiento que quita la clase del diagrama.
	 */
	public void removeClass(Class c) {
		if (lastHover == c) {	// Evitamos dependencias
			lastHover = null;
		}

		modelo.removeClass(c);
	}

	/*
	 * Pre:  Dado una clase "c" y unas nuevas coordenadas "x" e "y".
	 * Post: Este procedimiento mueve la clase "c" a las coordenadas designadas.
	 */
	public void moveClass(Class c, int x, int y) {
		modelo.moveClass(c, x, y);
	}

	/*
	 * Pre:  Dado una clase "c" y un booleano "selected" que representa el estado de
	 * 		 selección sobre la clase.
	 * Post: Este procedimiento cambia el estado de selección por el que se pasa por
	 * 		 parámetro.
	 */
	public void selectClass(Class c, boolean selected) {
		modelo.selectClass(c, selected);
	}

	/*
	 * Pre:  Dado dos clases "c1" y "c2", con la asociación "a".
	 * Post: Este procedimiento genera la relación de asociación entre las clases.
	 */
	public void addAssociation(Association a, Class c1, Class c2) {
		modelo.addAssociation(a);

		c1.addAssociation(a);
		c2.addAssociation(a);
	}

	/*
	 * Pre:  Dado la gráfica "g".
	 * Post: Procedimiento referenciado cada vez que sea necesario actualizar el estado de
	 * 		 algún elemento del diagrama.
	 */
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);

		for(Association a : associations) {
			a.draw(g);
		}

		for(Class c : classes) {
			c.draw(g);
		}

		// Traemos al frente la clase semioculta sobre la que está el ratón
		if(lastHover != null) {
			lastHover.draw(g);
		}
	}
	
	/********************************************/
	/** Métodos de MouseListener               **/
	/********************************************/

	/*
	 * Pre:  Dado un evento de ratón "e".
	 * Post: Procedimiento que gestiona la presión sobre el ratón.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			Class c = punteroEnClase(e.getX(), e.getY());
			if(c != null && clase != c) {
				dragging = true;
			}
			else if(c != null) {
				associating = true;
			}
		}
	}
    
	/*
	 * Pre:  Dado un evento de ratón "e".
	 * Post: Procedimiento que gestiona las acciones al soltar el ratón.
	 */
	@Override
    public void mouseReleased(MouseEvent e) {
		Class c = punteroEnClase(e.getX(), e.getY());
		
		if(dragging && e.getButton() == MouseEvent.BUTTON1) {
			dragging = false;
			if(c != null) {
				moveClass(c, e.getX(), e.getY());
			}
		}
		else if(associating && e.getButton() == MouseEvent.BUTTON1) {
			associating = false;

			if(c != null) {
				addAssociation(
					new Association(clase.getX(), clase.getY(), c.getX(), c.getY(), clase, c),
					clase,
					c
				);

				modelo.hoverClass(c, false);
			}
		}
		lastX = e.getX();
		lastY = e.getY();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
    
	@Override
	public void mouseExited(MouseEvent e) {}
    
	/*
	 * Pre:  Dado un evento de ratón "e".
	 * Post: Procedimiento que gestiona las acciones al pulsar el ratón.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) { // Click derecho: eliminar clase
			Class c = punteroEnClase(e.getX(), e.getY());
			if(c != null) removeClass(c);
		}
    }

	/********************************************/
	/** Métodos de MouseMotionListener         **/
	/********************************************/  
	
	/*
	 * Pre:  Dado un evento de ratón "e".
	 * Post: Procedimiento que gestiona las acciones al mover el ratón.
	 */
    @Override
	public void mouseMoved(MouseEvent e) {
		lastX = e.getX();
		lastY = e.getY();
		Class c = punteroEnClase(lastX, lastY);
		if(c != lastHover){
			lastHover = c;
			repaint();
		}
	}
    
	/*
	 * Pre:  Dado un evento de ratón "e".
	 * Post: Procedimiento que gestiona las acciones al arrastrar el ratón.
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragging) {
			Class c = punteroEnClase(e.getX(), e.getY());
			if(c != null) {
				moveClass(c, e.getX(), e.getY());
			}
		}
		else if (associating) {
			Class c = punteroEnClase(e.getX(), e.getY());
			if (c != null) {
				if (lastHover != null) {
					modelo.hoverClass(lastHover, false);
				}
				modelo.hoverClass(c, true);
				lastHover = c;
			}
			else if (lastHover != null) {
				modelo.hoverClass(lastHover, false);
			}
		}
	}
    
	/********************************************/
	/** Métodos de KeyListener                 **/
	/********************************************/

	/*
	 * Pre:  Dado un evento de teclado "e".
	 * Post: Procedimiento que gestiona las acciones al pulsar una tecla.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
			Class nuevaClase = punteroEnClase(lastX, lastY);

			if (nuevaClase == null || nuevaClase == clase) {
				if (clase != null) {
					selectClass(clase, false);
					clase = null;
				}
       	 	}
			else {
				if (clase != null) {
					selectClass(clase, false);
				}
				selectClass(nuevaClase, true);
				clase = nuevaClase;
        	}
		}
	}
    
	@Override
	public void keyPressed(KeyEvent e) {
		//...
	}
    
	@Override
    public void keyReleased(KeyEvent e) {
		//...
    }

	/********************************************/
	/** Métodos auxiliares                     **/
	/********************************************/

	/*
	 * Pre:  Dado unas coordenadas "px" y "py".
	 * Post: Función que devuelve la clase en la que está puesta el ratón.
	 */
	Class punteroEnClase(int px, int py) {
		for(int i = classes.size() - 1; i >= 0; --i) {
			Class c = classes.get(i);
			if(c.contienePunto(px, py)) {
				return c;
			}
		}
		return null;
	}

	/********************************************/
	/** Métodos de KeyListener                 **/
	/********************************************/

	/*
	 * Pre:
	 * Post: Procedimiento que actualiza el diagrama cuando sea necesario.
	 */
	@Override
	public void update() {
		repaint();
	}
}
