import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//otros imports …


public class Association {

	/** Atributos */
	private int startX, startY, endX, endY;		// Coordenadas recta
	private final Class clase1, clase2;			// asociación entre dos clases

	/** Constructor */
	public Association(int startX, int startY, int endX, int endY,
		Class clase1, Class clase2) {
			this.startX = startX;
			this.startY = startY;
			this.endX = endX;
			this.endY = endY;
			this.clase1 = clase1;
			this.clase2 = clase2;
	}
	
	/** Dibujar componente */
	public void draw(Graphics graphics) {
		Graphics2D graphics2d = (Graphics2D)graphics;
		graphics2d.setColor(new Color(0, 112, 112));
		graphics2d.setStroke(new BasicStroke(2));
		if(startX == endX && startY == endY) {	// En caso de que sea una asociación reflexiva
			// Desde el punto inicial, hasta la derecha misma altura
			graphics2d.drawLine(startX, startY - 25, startX + 100, endY - 25);

			// Desde el punto anterior, hacia abajo
			graphics2d.drawLine(startX + 100, endY - 25, startX + 100, endY + 25);

			// Desde el punto anterior, hacia la derecha
			graphics2d.drawLine(startX + 100, endY + 25, endX, endY + 25);
		}
		else {	// En caso contrario
			graphics2d.drawLine(startX, startY, endX, endY);
		}
	}

	/** Funciones auxiliares */
	public void changePosition() {
		startX = clase1.getX();
		startY = clase1.getY();
		endX   = clase2.getX();
		endY   = clase2.getY();
	}
}
