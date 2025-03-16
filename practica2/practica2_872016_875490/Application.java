import java.awt.*;
import javax.swing.*;


public class Application {

	//metodos
	private static void createAndShowGUI(Component contents) {			
		//creamos el JFrame
		JFrame.setDefaultLookAndFeelDecorated(true); 
		JFrame frame = new JFrame("Practica 5");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//creamos la ventana y sus componentes y los anyadimos al JFrame	
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		frame.pack();
		
		//establecemos los atributos del JFrame
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			// Creo modelo
			Modelo modelo = Modelo.getInstance();

			// Crear la instancia de Window y obtener JPanel
			Window window = new Window();
			JPanel panel = (JPanel) window.createComponents(modelo);

			// Crear el JFrame
			createAndShowGUI(panel);
		});
	}
}
