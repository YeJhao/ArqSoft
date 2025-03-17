//-------------------------------------------------------------------------------------------
// File:   Class.java
// Author: Jorge Soria Romero (872016) y Jiahao Ye (875490)
// Date:   17 de marzo de 2025
// Coms:   Fichero java de la clase Class, de la práctica 2 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

// Clase Class
public class Class {
	/** Número de identificación de la clase */
	private static int id = 0;

	private int x, y;			// Coordenadas del diagrama
	private int width, height;	// Ancho y alto de la clase en el diagrama
	private boolean selected;	// Indica si la clase está seleccionada
	private boolean candidate;	// Indica si es candidata a formar para de la asociación
	private final String nom;	// Nombre de la clase
	private final Vector<Association> associations;	// Asociaciones que mantiene
	
	/** Constructor */
	public Class(int x, int y) {
		this.x = x;
		this.y = y;
		this.nom = "Class " + String.valueOf(id);	
		this.selected = false;
		this.candidate = false;
		this.associations = new Vector<>();
		++id;	// Aumentamos para la siguiente clase creada
	}
	
	/*
	 * Pre:	 Dado una gráfica "g" en la que se puede dibujar.
	 * Post: Procedimiento que dibuja en pantalla la clase.
	 */
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		Font font = new Font("Arial", Font.BOLD, 14);
		g2.setFont(font);
		
		Color fill_color = Color.WHITE;
		Color bord_color = new Color(0, 112, 112);

		if(selected) {	// Si se selecciona, lo ponemos azul
			fill_color = new Color(0, 255, 255);
		}

		if(candidate) {	// Si se pasa por encima, se pone verde
			fill_color = new Color(144, 240, 144);
			bord_color = new Color(0, 112, 0);
		}

		int padding_h = 16, padding_v = 4;
		height = 60;

		FontMetrics fm = g2.getFontMetrics();

		width = Math.max(
			fm.stringWidth(nom),
			fm.stringWidth("atributos")
		) + 2 * padding_h; //16 px de padding horizontal

		int left = x - width / 2;
		int right = left + width;
		int top = y - height / 2;

		// Dibujamos la forma
		g2.setColor(fill_color);
		g2.fillRect(left, top, width, height);
		g2.setColor(bord_color);
		g2.setStroke(new BasicStroke(2));
		g2.drawRect(left, top, width, height);
		g2.setStroke(new BasicStroke());
		g2.drawLine(left, top + 20, right, top + 20);
		g2.drawLine(left, top + 40, right, top + 40);
		
		// Rellenamos las palabras necesarias
		g2.setColor(Color.BLACK);
		g2.drawString(nom, left + padding_h, top + 20 - padding_v);
		g2.drawString("atributos", left + padding_h, top + 40 - padding_v);
		g2.drawString("métodos", left + padding_h, top + 60 - padding_v);
	}
	
	/*
	 * Pre:
	 * Post: Función que devuelve la coordenada x de la clase.
	 */
	public int getX() { return this.x; }

	/*
	 * Pre:
	 * Post: Función que devuelve la coordenada y de la clase.
	 */
	public int getY() { return this.y; }

	/*
	 * Pre:
	 * Post: Función que devuelve el ancho de la clase.
	 */
	public int getWidth() { return this.width; }

	/*
	 * Pre:
	 * Post: Función que devuelve la altura de la clase.
	 */
	public int getHeight() { return this.height; }

	/*
	 * Pre:
	 * Post: Función que devuelve la colección de asociaciones que mantiene la clase.
	 */
	public Vector<Association> associations() { return associations; }

	/*
	 * Pre:	 Dado dos enteros que representan la posición sobre un diagrama.
	 * Post: Esta operación cambia las coordenadas de la clase y las asociaciones respectivas.
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;

		// Cambiamos la posición de las asociaciones, con las nuevas coordenadas de la clase
		for(Association a : associations) {
			a.changePosition();
		}
	}

	/*
	 * Pre:	 Dado un booleano "selected".
	 * Post: El siguiente procedimiento cambia el "selected" por el que se pasa por parámetro.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/*
	 * Pre:	 Dado un booleano "candidate".
	 * Post: El siguiente procedimiento cambia el "candidate" por el que se pasa por parámetro.
	 */
	public void setCandidate(boolean candidate) {
		this.candidate = candidate;
	}

	/*
	 * Pre:
	 * Post: Esta operación elimina todas las asociaciones almacenadas por la clase.
	 */
	public void deleteAssociations() {
		associations.clear();
	}

	/*
	 * Pre:
	 * Post: Procedimiento que añade una asociación a la clase.
	 */
	public void addAssociation(Association a) {
		associations.add(a);
	}

	/*
	 * Pre:	 Dado dos enteros que indican las coordenadas en el diagrama.
	 * Post: Función que devuelve <<true>>, si las coordenadas pasadas concuerdan con algún
	 * 		 punto contenido en la clase. En caso contrario, devuelve <<false>>.
	 */
	public boolean contienePunto(int px, int py) {
		int x_ = x - width / 2;
		int y_ = y - height / 2;
		return (px >= x_ && px <= x_ + width) && (py >= y_ && py <= y_ + height);
	}
}
