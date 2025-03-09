import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

//otros import

public class Class {
	
	//Atributos
	private int x;		// Coordenadas del
	private int y;		// diagrama.
	private int width;	// Ancho clase en el diagrma.
	private int height;	// Alto  clase en el diagrama.
	String nom;			// Nombre de la clase
	
	public Class(int x, int y, String nom) {
		this.x = x;
		this.y = y;
		this.nom = nom;	
	}
	
	public void draw(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		
		Font font = new Font("Arial", Font.BOLD, 14);
		g2.setFont(font);
		
		Color fill_color = new Color(144, 240, 144);
        Color bord_color = new Color(0, 112, 0);

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
	
	public int getX() { return this.x; }

	public int getY() { return this.y; }

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getWidth() { return this.width; }

	public int getHeight() { return this.height; }

	public boolean contienePunto(int px, int py) {
		int x_ = x - width/2;
		int y_ = y - height/2;
		return (px >= x_ && px <= x_ + width) && (py >= y_ && py <= y_ + height);
	}
}
