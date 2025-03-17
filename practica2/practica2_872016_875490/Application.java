//-------------------------------------------------------------------------------------------
// File:   Application.java
// Author: Jorge Soria Romero (872016) y Jiahao Ye (875490)
// Date:   17 de marzo de 2025
// Coms:   Fichero java de la clase Application, de la práctica 2 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

import java.awt.*;
import javax.swing.*;

// Clase Application
public class Application {
	/*
	 * Pre:	 Dado una componente que se puede mostrar en pantalla.
	 * Post: El siguiente procedimiento crea y muestra una interfaz de usuario con "contents",
	 * 		 en pantalla.
	 */
	private static void createAndShowGUI(Component contents) {			
		// Creamos el JFrame
		JFrame.setDefaultLookAndFeelDecorated(true); 
		JFrame frame = new JFrame("Practica 2");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Creamos la ventana y sus componentes y los añadimos al JFrame	
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		frame.pack();
		
		// Establecemos los atributos del JFrame
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/*
	 * Programa principal que implementa un editor de diagramas de clases.
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			// Creo un modelo vacío
			Modelo modelo = Modelo.getInstance();

			// Crear la instancia de Window y obtener JPanel
			Window window = new Window();
			JPanel panel = (JPanel) window.createComponents(modelo);

			// Crear el JFrame
			createAndShowGUI(panel);
		});
	}
}
