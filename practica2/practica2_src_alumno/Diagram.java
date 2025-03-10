import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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
	private boolean dragging;		// Arrastrando el ratón
	private int lastX, lastY;		// Últimas coordenadas del ratón


	/** Constructor */
	public Diagram(Modelo modelo) {	
		setBorder(BorderFactory.createLineBorder(Color.black));
		this.modelo = modelo;
		dragging = false;
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

	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		for(Class c : modelo.classes()) {
			c.draw(g);
		}
	}
	
	/********************************************/
	/** Métodos de MouseListener               **/
	/********************************************/

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(punteroEnClase(e.getX(), e.getY()) != null) {
				dragging = true;
			}
		}
	}
    
	@Override
    public void mouseReleased(MouseEvent e) {
		if(dragging && e.getButton() == MouseEvent.BUTTON1) {
			dragging = false;
			Class c = punteroEnClase(e.getX(), e.getY());
			if(c != null) moveClass(c, e.getX(), e.getY());
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
	}
    
	@Override
	public void mouseDragged(MouseEvent e) {
		if(dragging) {
			Class c = punteroEnClase(e.getX(), e.getY());
			if(c != null) moveClass(c, e.getX(), e.getY());
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
		ArrayList<Class> classes = modelo.classes();
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
