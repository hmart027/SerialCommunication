package comm.main;
import gnu.io.*;


public class picDriver {
	
	Comt com1=new Comt();
	SerialPort Port;
	int intosend;
	
	public picDriver(){
		
	}
	
	//Initializing port communications with pic: Port & Baudrate are given
	public void initDriver(String port, int baudrate){
		
		Port=com1.getComm(port,baudrate);
		
	}
	
	//Initializing port communications with pic: Port is given
	public void initDriver(String port){
		
		Port=com1.getComm(port);
		
	}
	
	//Preparing and sending a command or request to pic
	public void sendtoPic(char type, String command){
		
		//what is it command or request
		switch (type){
		
		case 'c':
			intosend=0b01000000;
			sendCommand(command);
			break;
			
		case 'r':
			intosend=0b10000000;
			sendCommand(command);
			break;
			
		default:
			intosend=0;
			sendCommand(command);
		}
		
		
	}
	
	//to send command
	public void sendCommand(String command){
		
		//what command is
		switch (command){
				
			case "wp":
				com1.sendInfo(Port, intosend+0b0101);
				com1.sendInfo(Port, intosend+0b1001);
				break;
			
			case "wr":
				com1.sendInfo(Port, intosend+0b0100);
				com1.sendInfo(Port, intosend+0b1000);
				break;
				
			case "sp":
				com1.sendInfo(Port, intosend+0b0110);
				com1.sendInfo(Port, intosend+0b1010);
				break;	
				
			case "sr":
				com1.sendInfo(Port, intosend+0b0100);
				com1.sendInfo(Port, intosend+0b1000);
				break;
				
				
				}
		
	}

}
