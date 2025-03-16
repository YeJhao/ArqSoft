import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

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
	public Class lastHover;
	private boolean dragging;		// Arrastrando el ratón
	private boolean associating;	// Creando asociación
	private int lastX, lastY;		// Últimas coordenadas del ratón

	private final Vector<Class> classes;
	private final Vector<Association> associations;


	/** Constructor */
	public Diagram(Modelo modelo) {	
		setBorder(BorderFactory.createLineBorder(Color.black));
		this.modelo = modelo;
		dragging = false;
		associating = false;
		classes = modelo.classes();
		associations = modelo.associations();
	}

	/** Iniciar */
	public void init() {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		modelo.attach(this, Aspecto.ALL);
	}
	
	/** Añadir una clase */
	public void addClass(int x, int y) {
		modelo.addClass(new Class(x, y));
	}

	/** Quitar una clase */
	public void removeClass(Class c) {
		if(lastHover == c) {
			lastHover = null;
		}
		modelo.removeClass(c);
	}

	/** Cambiar la posición de una clase */
	public void moveClass(Class c, int x, int y) {
		modelo.moveClass(c, x, y);
	}

	/** Seleccionar o deseleccionar clase */
	public void selectClass(Class c, boolean selected) {
		modelo.selectClass(c, selected);
	}

	/** Añadir una asociación */
	public void addAssociation(Association a, Class c1, Class c2) {
		modelo.addAssociation(a);
		c1.addAssociation(a);
		c2.addAssociation(a);
	}

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

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			Class c = punteroEnClase(e.getX(), e.getY());
			if(c != null && clase != c) {
				dragging = true;
			} else if(c != null) {
				associating = true;
			}
		}
	}
    
	@Override
    public void mouseReleased(MouseEvent e) {
		Class c = punteroEnClase(e.getX(), e.getY());
		
		if(dragging && e.getButton() == MouseEvent.BUTTON1) {
			dragging = false;
			if(c != null) {
				moveClass(c, e.getX(), e.getY());
			}
		} else if(associating && e.getButton() == MouseEvent.BUTTON1) {
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
    
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) { //Click derecho: eliminar clase
			Class c = punteroEnClase(e.getX(), e.getY());
			if(c != null) removeClass(c);
		}
    }

	/********************************************/
	/** Métodos de MouseMotionListener         **/
	/********************************************/    
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
    
	@Override
	public void mouseDragged(MouseEvent e) {
		if(dragging) {
			Class c = punteroEnClase(e.getX(), e.getY());
			if(c != null) {
				moveClass(c, e.getX(), e.getY());
			}
		} else if(associating) {
			Class c = punteroEnClase(e.getX(), e.getY());
			if(c != null) {
				if(lastHover != null) {
					modelo.hoverClass(lastHover, false);
				}
				modelo.hoverClass(c, true);
				lastHover = c;
			} else if(lastHover != null) {
				modelo.hoverClass(lastHover, false);
			}
		}
	}
    
	/********************************************/
	/** Métodos de KeyListener                 **/
	/********************************************/
	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
			Class nuevaClase = punteroEnClase(lastX, lastY);
			if (nuevaClase == null || nuevaClase == clase) {
				if (clase != null) {
					selectClass(clase, false);
					clase = null;
				}
       	 	} else {
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
	@Override
	public void update() {
		repaint();
	}
}
