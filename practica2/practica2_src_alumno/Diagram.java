import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Diagram 
		extends JPanel 
		implements MouseListener, 
			   MouseMotionListener, 
			   KeyListener {
	
	//atributos
	private Window window; // Ventana en la que está el diagrama
	public Class clase; 
	
	private Vector<Class> classes = new Vector<>(); // las clases que crea el usuario
	private Vector<Association> associations = new Vector<>(); // las asociaciones que crea el usuario
	
	// ... (otros posibles atributos)


	//metodos
	public Diagram(Window theWindow) {
		window = theWindow;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	public void addClass(int x, int y, String nom) {
		classes.add(
			new Class(x, y, nom)
		);
	}

	public void removeClass(int x, int y) {
		classes.remove(
			null
		);
	}
	
	public int getNClasses(){
		return classes.size();
	}
	
	public int getNAssociations(){
		return associations.size();
	}

	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		for(Class c : classes) {
			c.draw(g);
		}
	}
	
	/********************************************/
	/** Métodos de MouseListener               **/
	/********************************************/

	@Override
	public void mousePressed(MouseEvent e) {
		// ...
	}
    
	@Override
    public void mouseReleased(MouseEvent e) {
 		//...
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//...
    }
    
	@Override
	public void mouseExited(MouseEvent e) {
		//...
    }
    
	@Override
	public void mouseClicked(MouseEvent e) {
		//...
    }

	/********************************************/
	/** Métodos de MouseMotionListener         **/
	/********************************************/    
    @Override
	public void mouseMoved(MouseEvent e) {
		//...
	}
    
	@Override
	public void mouseDragged(MouseEvent e) {
		//...
	}
    
	/********************************************/
	/** Métodos de KeyListener                 **/
	/********************************************/
	@Override
	public void keyTyped(KeyEvent e) {
		//...
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
		for(Class c : classes) {
			if(c.contienePunto(px, py)) {
				return c;
			}
		}
		return null;
	}
}
