package comm.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.TooManyListenersException;

import gnu.io.*;

public class Comm {
	
	public static int DEFAULT_BAUD = 9600;

	private volatile boolean state = false;
	private SerialPort serialPort = null; // Serial port object
	public boolean dataRdy = false;

	private byte[] buffer = new byte[300];
	private int byteNum = 0;
	private String bufferS = null;
	
	private LinkedList<InputStreamListener> listeners = new LinkedList<>();

	public Comm() {}
	
	// Get list of serial ports
	@SuppressWarnings("rawtypes")
	public static ArrayList<String> getSerialPortList() {

		// Array containing the name of the ports
		ArrayList<String> list = new ArrayList<String>();

		// List of all ports
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList
					.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				list.add(portId.getName()); // If the port is a serial port add
											// to list
			}
		}
		return list;
	}
	
	public static Comm getCommInstance(String port){
		Comm c = new Comm();
		c.getComm(port);
		if(c.state) 
			return c;
		return null;
	}
	
	public static Comm getCommInstance(String port, int baud){
		Comm c = new Comm();
		c.getComm(port, baud);
		if(c.state) 
			return c;
		return null;
	}

	public String getPort() {
		String portName = null;
		if (serialPort != null) {
			portName = serialPort.getName();
			portName = portName.substring(4, portName.length());
		}
		return portName;
	}

	// Returns the value of Status
	public boolean getState() {
		return state;
	}

	// Disposes the opened port
	public void closeComm() {
		state = false;
		if (serialPort != null)
			serialPort.close();
	}

	// Without a explicit baudrate
	public boolean getComm(String port) {
		return getComm(port, DEFAULT_BAUD);
	}

	// With a explicit baudrate
	public boolean getComm(String port, int baudrate) {

		if (serialPort != null)
			serialPort.close();

		CommPortIdentifier portId; // Identifier for ports

		try {

			// Get the serial port ID
			portId = CommPortIdentifier.getPortIdentifier(port);

			// Open and get ownership of the port
			serialPort = (SerialPort) portId.open("Java Serial Com Lib", 1000);

			// Setting Parameters
			serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
//			(new SerialEventListenerThread()).start();		// Start ListenerThread
			serialPort.addEventListener(new SerialEventListenerThread());
			serialPort.notifyOnDataAvailable(true);
			state = true;
		} catch (NoSuchPortException e1) {
			e1.printStackTrace();
		} catch (PortInUseException e) {
			System.out.println("Port in use");
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		return state;
	}

	// Method to send information to client
	public void sendInfo(int cmd) {

		// Dont send if there is no connection
		if (state) {
			try {

				// Create the output streams
				OutputStream out = serialPort.getOutputStream();

				// Sending txt to client
				out.write(cmd);

			} catch (IOException e) {
				System.out.println("IO Exception");
			}
		}
	}

	public boolean sendByteArray(byte[] data) {
		// Dont send if there is no connection
		if (state) {
			try {

				// Create the output streams
				OutputStream out = serialPort.getOutputStream();

				// Sending txt to client
				for(byte i: data){
					out.write(i);
					}
				return true;
			} catch (IOException e) {
				System.out.println("IO Exception");
			}
		}
		return false;
	}
	
	public boolean sendByteArray(byte[] data, long sleepFor) throws InterruptedException {
		// Dont send if there is no connection
		if (state) {
			try {

				// Create the output streams
				OutputStream out = serialPort.getOutputStream();

				// Sending txt to client
				for(byte i: data){
					out.write(i);
					Thread.sleep(sleepFor);
				}
				return true;
			} catch (IOException e) {
				System.out.println("IO Exception");
			}
		}
		return false;
	}

	public boolean sendString(String data) {
		if (state) {
			try {
				// Create the output streams
				OutputStream output = serialPort.getOutputStream();

				// Sending txt to client
				output.write(data.getBytes());
			} catch (IOException e) {
				System.out.println("IO Exception");
			}
		}
		return false;
	}

	// Method to receive information from client
	public String getStringInfo() {
		if (dataRdy) {
			return bufferS;
		}
		return null;
	}

	// Method to receive information from client
	public byte[] getByteInfo() {
		byte[] out = new byte[byteNum];
		if (dataRdy) {
			for (int i = 0; i < byteNum; i++) {
				out[i] = buffer[i];
			}
			return out;
		}
		return null;
	}

	public InputStream getInputStream() {
		try {
			return serialPort.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public OutputStream getOutputStream() {
		if(!state) return null;
		try {
			return serialPort.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isConnected(){
		return getState();
	}

	public void addInputStreamListener(InputStreamListener i){
		listeners.add(i);
	}
	
	public class SerialEventListenerThread implements SerialPortEventListener{
		InputStream in = null;
		
		SerialEventListenerThread(){
			this.in = getInputStream();
		}

		@Override
		public void serialEvent(SerialPortEvent arg0) {
			try {
				int d;
				while((d = in.read())>-1){
					for(InputStreamListener l: listeners){
						l.onByteReceived(d);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}