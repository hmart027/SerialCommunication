package comm.main;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import gnu.io.*;

public class Comt {

	int stat=0;
	
 public Comt (){

	}
 
 	//Without a explicit baudrate
 public SerialPort getComm(String port){
	 

	 CommPortIdentifier portId;	        //Identifier for ports
	 SerialPort serialPort=null;		//Serial port object
	 
	 try {
			
			// Get the serial port ID
			portId= CommPortIdentifier.getPortIdentifier(port);
			
			// Open and get ownership of the port
			serialPort= (SerialPort) portId.open("SimpleWriteApp",
	                1000);
		    
		 // Setting Parameters
		    serialPort.setSerialPortParams(9600,
			        SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
			        SerialPort.PARITY_NONE);
			
			stat=1;
	
	 	}catch (NoSuchPortException e1) {
	 		e1.printStackTrace();
			
		}  catch (PortInUseException e) {
			System.out.println("Port in use");
		
        }  catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		}
	 return serialPort;
 }
 
 	//With a explicit baudrate
 public SerialPort getComm(String port, int baudrate){
	 

	 CommPortIdentifier portId;	//Identifier for ports
	 SerialPort serialPort = null;		//Serial port object
	 
	 try {
			
			// Get the serial port ID
			portId= CommPortIdentifier.getPortIdentifier(port);
			
			// Open and get ownership of the port
			serialPort = (SerialPort) portId.open("SimpleWriteApp",
		                1000);
		    
		 // Setting Parameters
			serialPort.setSerialPortParams(baudrate,
			        SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
			        SerialPort.PARITY_NONE);
			
			stat=1;

	
	 	}catch (NoSuchPortException e1) {
			e1.printStackTrace();
			
		}  catch (PortInUseException e) {
			System.out.println("Port in use");
		
        }  catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		}
	 return serialPort;
 }
 
 
 	//Method to send information to client
  public void sendInfo(SerialPort serialPort, int cmd){
	  	
	//Dont send if there is no connection
	  if (stat==1){ 
				try {
					
					// Create the output streams  
					OutputStream out=serialPort.getOutputStream();
					 			
					// Sending txt to client 
					out.write(cmd);	
					
				} catch (IOException e) {
		            System.out.println("IO Exception");
		        }			
		}	
  }
  
  
  	// Method to receive information from client
  public char receiveInfo(SerialPort serialPort){
	  
	  char txt = 0;	//character variable
	  
	  if (stat==1){ //Don't send if there is no connection
			try {
				
				//Create input stream
				InputStream in = serialPort.getInputStream();
				
				if(in.available()>0){
				//Convert the int into char
				txt = (char)in.read();
				}

				//Rest the Stream
				in.close();
				
			} catch (IOException e) {
	            System.out.println("IO Exception");
	        }			
	}	
	  
	  return txt;
	  
  }
			
}

