package comm.main;
// Frame for the Sketcher application
import javax.swing.*;

import java.awt.Dimension;
import java.awt.Font;

@SuppressWarnings("serial")
public class Window extends JFrame {

	// Constructor
	public Window(String title) {
		
		setTitle(title); // Set the window title
		setDefaultCloseOperation(EXIT_ON_CLOSE);

}
	
	public JLabel addLabel(String str,int xaling,int yaling){
		
		//Trying to add a label
		Font font = new Font("Times New Roman", Font.PLAIN, 12);
		JLabel textLabel = new JLabel(str);
		textLabel.setFont(font);
		textLabel.setHorizontalAlignment(SwingConstants.LEFT);
		textLabel.setVerticalAlignment(SwingConstants.BOTTOM );
		return textLabel;
		
	}
	
	public JLayeredPane addPane(int x, int y, int widh,int heigth){
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(widh,heigth));
		
		return layeredPane;	
	}

}