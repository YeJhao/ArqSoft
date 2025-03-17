//-------------------------------------------------------------------------------------------
// File:   Window.java
// Author: Jorge Soria Romero (872016) y Jiahao Ye (875490)
// Date:   17 de marzo de 2025
// Coms:   Fichero java de la clase Window, de la práctica 2 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Clase Window
public class Window implements ActionListener {

	/** Atributos */
	private Diagram diagram; // Superficie de dibujo
	private final JPanel panel; 
	private final GridBagConstraints c;
	private Etiqueta labelNClasses;			// Etiqueta para mostrar el número de clases
	private Etiqueta labelNAssociations;	// Etiqueta para mostrar el número de asociaciones
	
	/** Constructor */
	public Window() {
		super();
		// Creamos el panel y el grid
		panel = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
	}
	
    /*
	 * Pre:	 Dado un modelo.
	 * Post: Se crean los componentes necesarios para poder operar las acciones implementadas.
	 */
	public Component createComponents(Modelo modelo) {
		// Creamos el boton de añadir clases y lo ponemos en el panel
		JButton button = new JButton("Add Class");
		button.setMnemonic(KeyEvent.VK_I);
		button.addActionListener(this);
 		setGridProperties(0,0,1,1,0,0,GridBagConstraints.NONE);
	    panel.add(button, c);

		// Creamos el diagrama y lo ponemos en el panel
		diagram = new Diagram(modelo); diagram.init();
		setGridProperties(0,1,4,3,1.0,1.0,GridBagConstraints.BOTH);
      	panel.add(diagram, c);
  
		// Creamos la etiqueta para contar clases y lo ponemos en el panel 
		labelNClasses = new Etiqueta("Classes: " + modelo.getNClasses(), modelo, Aspecto.CLASS);
		labelNClasses.init();
		setGridProperties(0,4,2,1,0,0,GridBagConstraints.HORIZONTAL);
        panel.add(labelNClasses, c);
        
		// Creamos la etiqueta para contar asociaciones y lo ponemos en el panel 
		labelNAssociations = new Etiqueta("Associations: " + modelo.getNAssociations(), modelo, Aspecto.ASSOCIATION);
		labelNAssociations.init();
		setGridProperties(2,4,2,1,0,0,GridBagConstraints.HORIZONTAL);
        panel.add(labelNAssociations, c);
		
		// Ajustes del panel
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		return panel;
	}
	
	/*
	 * Pre:	 Dado una serie de parámetros estéticos.
	 * Post: Procedimiento que define el grid con los parámetros que se incluyen.
	 */
	private void setGridProperties(int gridx, int gridy, int gridwidth, int gridheight,
								   double weightx, double weighty, int fill) {
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
		c.gridheight = gridheight;
		c.weightx = weightx;
		c.weighty = weighty;
		c.fill = fill;
	}

	/*
	 * Pre:	 Dado una acción "e".
	 * Post: Procedimiento que añade una clase.
	 */
	@Override
	public void actionPerformed(ActionEvent e){
		diagram.addClass(panel.getWidth() / 2, panel.getHeight() / 2);
		diagram.requestFocusInWindow();
	}

	/*
	 * Programa principal que prepara el panel donde se situará el editor de diagramas de clases.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			// Creo modelo
			Modelo modelo = Modelo.getInstance();

			// Crear el JFrame
			JFrame frame = new JFrame("Diagram Editor");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// Crear la instancia de Window y obtener el JPanel
			Window window = new Window();
			JPanel panel = (JPanel) window.createComponents(modelo);

			// Agregar el panel al frame
			frame.getContentPane().add(panel);

			// Configurar tamaño y mostrar la ventana
			frame.setSize(600, 400);
			frame.setLocationRelativeTo(null); // Centrar en pantalla
			frame.setVisible(true);
		});
	}

	// Clase privada para gestionar una Etiqueta
	private class Etiqueta extends JLabel implements Observador {
		private final Aspecto asp;
		private final Modelo modelo;

		/** Constructor */
		public Etiqueta(String contenido, Modelo modelo, Aspecto asp) {
			super(contenido);
			this.modelo = modelo;
			this.asp = asp;
		}

		/*
		* Pre:
		* Post: Procedimiento que asigna una relación de observación para la etiqueta.
		*/
		public void init() { modelo.attach(this, asp); }

		/*
		* Pre:
		* Post: Procedimiento que actualiza la etiqueta dependiendo del sujeto.
		*/
		@Override
		public void update() {
			setText(switch (asp) {
            	case CLASS -> "Classes: " + modelo.getNClasses();
            	case ASSOCIATION -> "Associations: " + modelo.getNAssociations();
            	default -> getText();
        	});
			repaint();
		}
	}
}
