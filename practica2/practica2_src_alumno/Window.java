import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Window implements ActionListener {

	//atributos
	private Diagram diagram; //superficie de dibujo
	private final JPanel panel; 
	private final GridBagConstraints c;
	private JLabel labelNClasses;
	private JLabel labelNAssociations;
	

	//metodos
	public Window() {
		super();
		//creamos el panel y el grid
		panel = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
	}
	
	public Component createComponents() {
		//creamos el boton de añadir clases y lo ponemos en el panel
		JButton button = new JButton("Add Class");
		button.setMnemonic(KeyEvent.VK_I);
		button.addActionListener(this);
 		setGridProperties(0,0,1,1,0,0,GridBagConstraints.NONE);
	        panel.add(button, c);

		//creamos el diagrama y lo ponemos en el panel
		diagram = new Diagram(this);
		setGridProperties(0,1,4,3,1.0,1.0,GridBagConstraints.BOTH);
      		panel.add(diagram, c);
  
		//creamos la etiqueta para contar clases y lo ponemos en el panel 
	        labelNClasses = new JLabel("Classes: " + diagram.getNClasses());
		setGridProperties(0,4,2,1,0,0,GridBagConstraints.HORIZONTAL);
        	panel.add(labelNClasses, c);
        
		//creamos la etiqueta para contar asociaciones y lo ponemos en el panel 
	        labelNAssociations = new JLabel("Associations: " + diagram.getNAssociations());
		setGridProperties(2,4,2,1,0,0,GridBagConstraints.HORIZONTAL);
        	panel.add(labelNAssociations, c);
		
		//ultimos ajustes del panel
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return panel;
	}
	
	private void setGridProperties(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int fill)
{
     		c.gridx = gridx;
        	c.gridy = gridy;
        	c.gridwidth = gridwidth;
        	c.gridheight = gridheight;
        	c.weightx = weightx;
        	c.weighty = weighty;
		c.fill = fill;
	}

	@Override
	public void actionPerformed(ActionEvent e){
		diagram.addClass(panel.getWidth() / 2, panel.getHeight() / 2, "Esta clase es largísima");
		diagram.requestFocusInWindow();
	}
	
	public void updateNClasses(Diagram d){
		labelNClasses.setText("Classes: " + d.getNClasses());
	}
	
	public void updateNAssociations(Diagram d){
		labelNAssociations.setText("Associations: " + diagram.getNAssociations());
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			// Crear el JFrame
			JFrame frame = new JFrame("Diagram Editor");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// Crear la instancia de Window y obtener el JPanel
			Window window = new Window();
			JPanel panel = (JPanel) window.createComponents();

			// Agregar el panel al frame
			frame.getContentPane().add(panel);

			// Configurar tamaño y mostrar la ventana
			frame.setSize(600, 400);
			frame.setLocationRelativeTo(null); // Centrar en pantalla
			frame.setVisible(true);
		});
	}
}
