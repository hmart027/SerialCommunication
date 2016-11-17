package comm.main;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.InputStream;

import gnu.io.*;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

public class Keytry implements WindowListener,KeyListener,ActionListener{
	
	char pkey=0;							// Key Pressed
	char poldkey=0;							// Previous Key Pressed
	char rkey=0;							// Key Released
	int keycode=0;
	static char txt=0;
    boolean hbr;							
    static String PORT=null;
    static String stus="Pick a Port";
    static JLabel status=new JLabel();	// The Status label
    static Comt com1=new Comt();
    static SerialPort port;
    static InputStream in;


	public static void main(String[] args) {
		
		 theApp = new Keytry (); // Create the application object
			SwingUtilities.invokeLater(
					new Runnable() { // Anonymous Runnable class object
						public void run() { // Run method executed in thread
							theApp.creatGUI(); // Call static GUI creator
						}
					} );
			
			while (true){
				
				if (port!=null){									//If port exist communicate
					
					txt=com1.receiveInfo(port);						//Get the info from the buffer
					if (txt!=0) System.out.println("Tank: "+txt);	//If data is different than 0 print
					
				}
			}	
	}
	
	//Method to Initialize the Connection
	public static void initConn(String Port){
		
		port=com1.getComm(Port);
		
		 //Checking for connection
		 if(com1.stat!=0){
			 stus="Connected to: "+Port;
			 System.out.println(stus);
				
		 }
		 else{
			 stus="Could Not Stablish Connection with: "+Port;
			 System.out.println(stus);
		 }
		 
	}
	
	// Method to create the application GUI
	private void creatGUI() {
			
			window = new Window("Tank Through COM"); // Create the app window
			Toolkit theKit = window.getToolkit(); // Get the window toolkit
			Dimension wndSize = theKit.getScreenSize(); // Get screen size

			// Set the position to screen center & size to half screen size
			window.setBounds(wndSize.width/4, wndSize.height/4, // Position
					wndSize.width/2, wndSize.height/2); // Size

			window.addWindowListener(this); // theApp as window listener
			window.addKeyListener(this);
			window.setVisible(true);

			//Set background image
			//window.getContentPane().add(new LoadImageApp("Jellyfish.jpg"));
			//window.pack();
			
			//Adding a Status Label
			status=window.addLabel(stus, wndSize.width, wndSize.height);
			window.getContentPane().add(status);
			
			//Adding a menu Bar
			window.setJMenuBar(menuBar); // Add the menu bar to the window
			JMenu portMenu = new JMenu("Ports"); // Create Elements menu
			menuBar.add(portMenu); // Add the element menu
			
			//Adding components to the menu bar
			JMenuItem Com1=new JMenuItem("COM1");
			JMenuItem Com2=new JMenuItem("COM2");
			portMenu.add(Com1);
			portMenu.add(Com2);
			
			//Adding action listeners for buttons
			Com1.addActionListener(this);
			Com2.addActionListener(this);
				
		}
		
	//Handling info to send toPIC
	public void picLanguage(char keypressed, boolean onof){
			
		//Pressing a letter
			switch(keypressed){
			
			case 'w':										//Forward
				if (onof==true){
					
					com1.sendInfo(port,0b01000101);
					com1.sendInfo(port,0b01001001);
					System.out.println("Forward");
					
				}else{
					com1.sendInfo(port,0b01000100);
					com1.sendInfo(port,0b01001000);}break;
				
			case 's':										//Backward
				if (onof==true){
					
					com1.sendInfo(port,0b01000110);
					com1.sendInfo(port,0b01001010);
					System.out.println("Backward");
					
				}else{
					com1.sendInfo(port,0b01000100);
					com1.sendInfo(port,0b01001000);}break;
				
			case 'a':										//Left
				if (onof==true){
					
					com1.sendInfo(port,0b01000110);
					com1.sendInfo(port,0b01001001);
					System.out.println("Left");
					
				}else{
					com1.sendInfo(port,0b01000100);
					com1.sendInfo(port,0b01001000);}break;
				
			case 'd':										//Right
				if (onof==true){
					
					com1.sendInfo(port,0b01000101);
					com1.sendInfo(port,0b01001010);
					System.out.println("Rigth");
					
				}else{
					com1.sendInfo(port,0b01000100);
					com1.sendInfo(port,0b01001000);}break;
				
			}
			
		//Pressing an Arrow
			switch(keycode){
			
			case 38:										//Forward
				if (onof==true){
					
					com1.sendInfo(port,0b01000101);
					com1.sendInfo(port,0b01001001);
					System.out.println("Forward");
					
				}else{
					com1.sendInfo(port,0b01000100);
					com1.sendInfo(port,0b01001000);}break;
				
			case 40:										//Backward
				if (onof==true){
					
					com1.sendInfo(port,0b01000110);
					com1.sendInfo(port,0b01001010);
					System.out.println("Backward");
					
				}else{
					com1.sendInfo(port,0b01000100);
					com1.sendInfo(port,0b01001000);}break;
				
			case 37:										//Left
				if (onof==true){
					
					com1.sendInfo(port,0b01000110);
					com1.sendInfo(port,0b01001001);
					System.out.println("Left");
					
				}else{
					com1.sendInfo(port,0b01000100);
					com1.sendInfo(port,0b01001000);}break;
				
			case 39:										//Right
				if (onof==true){
					
					com1.sendInfo(port,0b01000101);
					com1.sendInfo(port,0b01001010);
					System.out.println("Rigth");
					
				}else{
					com1.sendInfo(port,0b01000100);
					com1.sendInfo(port,0b01001000);}break;
				
			}			
			
		}
		
	//Handler for window closing event
	public void windowClosing(WindowEvent e) {
			
			System.out.println("bye");
			window.dispose(); 								// Release the window resources
			com1.sendInfo(port, 0b01000000);				// Sending final message to client
			System.exit(0); 								// End the application
		}	
		
	//Handler for pressing key event
	public void keyPressed(KeyEvent e){	
	
		 pkey=e.getKeyChar(); 	//define a key not to be repeated if still pressed
		 keycode=e.getKeyCode();
		 
		//Testing to see if the same key is still being pressed
		 if (pkey==rkey){		 
			 picLanguage(pkey, true);
			 rkey=0;}
		 	else if (pkey!=poldkey){
		 	 picLanguage(pkey, true);
		 	}
		 poldkey=pkey;
	}

	//Handler for releasing key event
	public void keyReleased(KeyEvent e){
		
		rkey=e.getKeyChar();
		picLanguage(rkey, false);
		
		//poldkey=0; //Once released reset the key so it can be pressed again
		
	}
	
	////Handler clicking buttons from drop menu
	public void actionPerformed(ActionEvent e) {
		
		if (com1.stat==1){
			com1.stat=0;
			port.close();
		}
		initConn(e.getActionCommand());
		status.setText(stus);
		status.repaint();
		
	}
	
	//Listener interface functions you must implement � but don�t need
	public void keyTyped(KeyEvent e){}
	public void windowOpened(WindowEvent e) { System.out.println("welcome");}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {System.out.println("welcome back");}
	public void windowDeactivated(WindowEvent e) {System.out.println("bye");}
	
	private Window window; 							// The application window
	private static Keytry theApp; 					// The application object	
	private JMenuBar menuBar = new JMenuBar(); // Window menu bar

}