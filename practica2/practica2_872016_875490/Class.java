import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

//otros import

public class Class {
	
	/** Número de creación de la clase */
	private static int id = 0;

	/** Atributos */
	private int x;				// Coordenadas del
	private int y;				// diagrama
	private int width;			// Ancho clase en el diagrma
	private int height;			// Alto  clase en el diagrama
	private boolean selected;	// Indica si la clase está seleccionada
	private boolean candidate;	// Indica si es candidata a formar para de la asociación
	private final String nom;	// Nombre de la clase
	private final Vector<Association> associations;
	
	/** Constructor */
	public Class(int x, int y) {
		this.x = x;
		this.y = y;
		this.nom = "Class " + String.valueOf(id);	
		this.selected = false;
		this.candidate = false;
		this.associations = new Vector<>();
		++id;
	}
	
	/** Dibujar componente */
	public void draw(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		
		Font font = new Font("Arial", Font.BOLD, 14);
		g2.setFont(font);
		
		Color fill_color = Color.WHITE;
		Color bord_color = new Color(0, 112, 112);
		if(selected) {
			fill_color = new Color(0, 255, 255);
		}
		if(candidate) {
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
		int top = y - height/2;

		g2.setColor(fill_color);
		g2.fillRect(left, top, width, height);
		g2.setColor(bord_color);
		g2.setStroke(new BasicStroke(2));
		g2.drawRect(left, top, width, height);
		g2.setStroke(new BasicStroke());
		g2.drawLine(left, top + 20, right, top + 20);
		g2.drawLine(left, top + 40, right, top + 40);
		
		g2.setColor(Color.BLACK);
		g2.drawString(nom, left + padding_h, top + 20 - padding_v);
		g2.drawString("atributos", left + padding_h, top + 40 - padding_v);
		g2.drawString("métodos", left + padding_h, top + 60 - padding_v);
	}
	
	/** Getters */
	public int getX() { return this.x; }

	public int getY() { return this.y; }

	public int getWidth() { return this.width; }

	public int getHeight() { return this.height; }

	public Vector<Association> associations() { return associations; }

	/** Setters */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		for(Association a : associations) {
			a.changePosition();
		}
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setCandidate(boolean candidate) {
		this.candidate = candidate;
	}

	/** Funciones auxiliares */
	public void deleteAssociations() {
		associations.clear();
	}

	public void addAssociation(Association a) {
		associations.add(a);
	}

	public boolean contienePunto(int px, int py) {
		int x_ = x - width/2;
		int y_ = y - height/2;
		return (px >= x_ && px <= x_ + width) && (py >= y_ && py <= y_ + height);
	}
}
