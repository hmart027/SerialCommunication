package comm.main;
import java.io.*;
import java.util.*;

import gnu.io.*;


public class Flashwriter implements SerialPortEventListener {
  @SuppressWarnings("rawtypes")
Enumeration portList;
  CommPortIdentifier portId;
  String messageString = "\r\nFLASH\r\n";
  SerialPort serialPort;
  OutputStream outputStream,outputStream2;
  InputStream inputStream;
  Thread readThread;
  String one, two;
  String test = "ONLINE";
  String[] dispArray = new String[1];
  int i = 0;

  byte[] readBufferArray;
  int numBytes;
  String response;
  FileOutputStream out;
  final int FLASH = 1, FILENAME = 2;
  int number;

  File winFile;

  public static void main(String[] args) throws IOException {
    Flashwriter sm = new Flashwriter();
    sm.FlashWriteMethod();
  }

  public void FlashWriteMethod() throws IOException {

    portList = CommPortIdentifier.getPortIdentifiers();
    winFile = new File("D:\\testing\\out.FLS");

    while (portList.hasMoreElements()) {
      portId = (CommPortIdentifier) portList.nextElement();
      if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        if (portId.getName().equals("COM2")) {
          // if (portId.getName().equals("/dev/term/a")) {
          try {
            serialPort = (SerialPort) portId.open("SimpleWriteApp",
                1000);
          } catch (PortInUseException e) {
          }

          try {
            inputStream = serialPort.getInputStream();

            System.out.println(" Input Stream... " + inputStream);
          } catch (IOException e) {
            System.out.println("IO Exception");
          }
          try {
            serialPort.addEventListener(this);

          } catch (TooManyListenersException e) {
            System.out.println("Tooo many Listener exception");
          }
          serialPort.notifyOnDataAvailable(true);

          try {
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
          } catch (IOException e) {
          }
          try {
            serialPort.setSerialPortParams(9600,
                SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
            serialPort
                .setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            // serialPort.disableReceiveTimeout();

            // outputStream.write(messageString.getBytes());

            // sendRequest("/r/n26-02-08.FLS/r/n");
            number = FLASH;
            sendRequest(number);

          } catch (UnsupportedCommOperationException e) {
          }

        }
      }
    }
  }

  @SuppressWarnings("unused")
public void serialEvent(SerialPortEvent event) {
    SerialPort port = (SerialPort) event.getSource();

    switch (event.getEventType()) {
    case SerialPortEvent.DATA_AVAILABLE:
      try {
        if (inputStream.available() > 0) {
          numBytes = inputStream.available();
          readBufferArray = new byte[numBytes];
          // int readtheBytes = (int) inputStream.skip(2);
          int readBytes = inputStream.read(readBufferArray);

          one = new String(readBufferArray);
          System.out.println("readBytes " + one);

        if (one.indexOf("FLASH_") > -1 & !(one.indexOf("FLASH_F") > -1)) {
          System.out.println("got message");
          response = "FLASH_OK";
          // JOptionPane.showMessageDialog(null,
          // "ONLINE",
          // "Online Dump",
          // JOptionPane.INFORMATION_MESSAGE);
          // Toolkit.getDefaultToolkit().beep();
          // outputStream.write("\r\nONLINEr\n".getBytes());
          // outputStream.flush();
          // outputStream.write("/r/n26-02-08.FLS/r/n".getBytes());

          number = FILENAME;
          sendRequest(number);
        }

        out = new FileOutputStream(winFile, true);
        out.write(readBufferArray);
        out.close();

        }
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      readBufferArray = null;
      // break;
    }

    // try {
    // int c;
    // while((c = inputStream.read()) != -1) {
    // out.write(c);
    // }
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // // readBufferArray=null;
    // break;
    // }
    // if (inputStream != null)
    // try {
    // inputStream.close();
    // if (port != null) port.close();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //      
    //

  }

  public void dispPacket(String packet) {
    if (response == "FLASH_OK") {
      System.out.println("disppacket " + packet);
    } else {
      System.out.println("No resust");
    }
  }

  public void sendRequest(int num) {
    switch (num) {
    case FLASH:
      try {
        outputStream.write(messageString.getBytes());
        System.out.println("Flash switch");
          outputStream.flush();

      } catch (IOException e) {
        e.printStackTrace();
      }
      break;
    case FILENAME:
      try {

        outputStream.write("\r\n26-02-08.FLS\r\n".getBytes());
        System.out.println("File name");
        outputStream.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
      break;

    }
  }

}