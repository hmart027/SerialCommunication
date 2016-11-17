package comm.main;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import gnu.io.*;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import comm.serial.Comm;

public class Mainwindow implements WindowListener,KeyListener{
	
	char pkey=0;							// Key Pressed
	char poldkey=0;							// Previous Key Pressed
	char rkey=0;							// Key Released
    boolean hbr;							
    static String PORT;
    static String stus;
    static JLabel status=new JLabel("");	// The Status label
    static Comm com1=new Comm();

	public static void main(String[] args) {
		
		
		PORT="COM1";
		com1.getComm(PORT);
		
	  theApp = new Mainwindow (); // Create the application object
		SwingUtilities.invokeLater(
				new Runnable() { // Anonymous Runnable class object
					public void run() { // Run method executed in thread
						theApp.creatGUI(); // Call static GUI creator
					}
				} );
		
			
	}
	
	//Method to Initialize the Connection
	public static void initConn(String ip,int port){
		
		 //Checking for connection
		 if(!com1.getState()){
			 stus="Connected to: "+PORT;
		 }else{stus="Could Not Stablish Connection with: "+PORT;}
		 
	}

	// Method to create the application GUI
	private void creatGUI() {
		
		window = new Window("Sketcher"); // Create the app window
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
		
			
	}

	//Handler for window closing event
	public void windowClosing(WindowEvent e) {
		
		System.out.println("bye");
		window.dispose(); // Release the window resources
		System.exit(0); // End the application
	}
	
	
	//Handler for pressing key event
	public void keyPressed(KeyEvent e){	
	
		 pkey=e.getKeyChar(); //define a key not to be repeated if still pressed
		 
		//Testing to see if the same key is still being pressed
		 if (pkey==rkey){
			 com1.sendString(Character.toString(pkey)+"p");
			 System.out.println(pkey+"p");
			 rkey=0;}
		 	else if (pkey!=poldkey){
			 com1.sendString(Character.toString(pkey)+"p");
			 System.out.println(pkey+"p");	
		 	}
		 poldkey=pkey;
	}

	//Handler for releasing key event
	public void keyReleased(KeyEvent e){
		
		rkey=e.getKeyChar();
		System.out.println(rkey+"r");
		com1.sendString(Character.toString(e.getKeyChar())+"r");
		
		//poldkey=0; //Once released reset the key so it can be pressed again
		
	}
	
	
	//Listener interface functions you must implement � but don�t need
	public void keyTyped(KeyEvent e){}
	public void windowOpened(WindowEvent e) { System.out.println("welcome");}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {System.out.println("welcome back");}
	public void windowDeactivated(WindowEvent e) {System.out.println("bye");}

	
private Window window; 								// The application window
private static Mainwindow theApp; 					// The application object

}