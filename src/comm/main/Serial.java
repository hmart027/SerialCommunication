package comm.main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import gnu.io.*;

public class Serial implements  SerialPortEventListener{

	static CommPortIdentifier portId;	//Identifier for ports
	static SerialPort serialPort;		//Serial port object
	static PrintWriter out;				//Data output stream
	static BufferedReader in;			//Data input stream
	
	static String txt=new String();		//String to write data

	

	public static void main (String[] args){

			try {
				
				// Get the serial port ID
				portId= CommPortIdentifier.getPortIdentifier("COM1");
				
				// Print port Name
				System.out.println(portId.getName());
				
				// Open and get ownership of the port
			    serialPort = (SerialPort) portId.open("SimpleWriteApp",
			                1000);
			    
			    // Create the input and output streams     
			    in =  new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			    out = new PrintWriter( serialPort.getOutputStream(),true) ;
			    BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
			    
			    // Setting Parameters
					serialPort.setSerialPortParams(9600,
					        SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					        SerialPort.PARITY_NONE);
								
				//Creating infinite loop
				while(true)	{
						
						//Starting Conversation	
							System.out.print("Me:");
							txt=kb.readLine();
							
						// Sending txt to socket output 
						//out.println( txt );	
						//System.out.println("Sent");
						
						//Reading incoming comm
						String line = in.readLine(); 
						System.out.println("Server:"+line);
							
					}
			         
				
			} catch (NoSuchPortException e1) {
				e1.printStackTrace();
				
			}  catch (PortInUseException e) {
				System.out.println("Port in use");
			
	        }  catch (IOException e) {
	            System.out.println("IO Exception");
	            
	        }catch (UnsupportedCommOperationException e) {
				e.printStackTrace();
			}			
	}		
		

	@Override
	public void serialEvent(SerialPortEvent event) {
		
		//Looking for event type
		switch (event.getEventType()) {
	    case SerialPortEvent.DATA_AVAILABLE:		//In case there is data available
	    	
	    	try {
	    		
	    		// Read response from socket 
				String line = in.readLine(); 
				System.out.println("Server:"+line);
	            
	            }catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	              }             
		}
	
	}
}
