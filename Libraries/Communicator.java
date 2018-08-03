/*
 * 	2017. 10. 21
 * 	Communicator.java
 * 	Created By D.Y. Jung
 */

package Libraries;

import gnu.io.*;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import Main.GUI;

public class Communicator implements SerialPortEventListener
{
	//passed from main GUI
	GUI window = null;

	//for containing the ports that will be found
	private Enumeration ports = null;
	//map the port names to CommPortIdentifiers
	private HashMap portMap = new HashMap();

	//this is the object that contains the opened port
	private CommPortIdentifier selectedPortIdentifier = null;
	private SerialPort serialPort = null;
	private int baudRate;

	//input and output streams for sending and receiving data
	private InputStream input = null;
	private OutputStream output = null;

	//just a boolean flag that i use for enabling
	//and disabling buttons depending on whether the program
	//is connected to a serial port or not

	//private int sensCounter = 0;

	private boolean bConnected = false;

	//the timeout value for connecting with the port
	final static int TIMEOUT = 2000;

	//some ascii values for for certain things
	final static int SPACE_ASCII = 32;
	final static int DASH_ASCII = 45;
	final static int NEW_LINE_ASCII = 10;


	//a string for recording what goes on in the program
	//this string is written to the GUI
	String logText = "";

	public Communicator(GUI window)
	{
		this.window = window;
	}

	//search for all the serial ports
	//pre: none
	//post: adds all the found ports to a combo box on the GUI
	public void searchForPorts()
	{
		ports = CommPortIdentifier.getPortIdentifiers();

		while (ports.hasMoreElements())
		{
			CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

			//get only serial ports
			if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
			{
				window.cmbPort.addItem(curPort.getName());
				portMap.put(curPort.getName(), curPort);
			}
		}
	}

	//connect to the selected port in the combo box
	//pre: ports are already found by using the searchForPorts method
	//post: the connected COM port is stored in commPort, otherwise,
	//an exception is generated
	public void connect()
	{
		String selectedPort = (String)window.cmbPort.getSelectedItem();
		selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

		CommPort commPort = null;

		try
		{
			//the method below returns an object of type CommPort
			commPort = selectedPortIdentifier.open("RoboLink Master Control Panel", TIMEOUT);
			//the CommPort object can be casted to a SerialPort object
			serialPort = (SerialPort)commPort;

			//for controlling GUI elements
			setConnected(true);

			//logging
			logText = selectedPort + " opened successfully.";
			
			baudRate = (int) window.cmbBaudRate.getSelectedItem();
			
			window.txtLog.setForeground(Color.black);
			window.txtLog.append(logText + "\n");

			window.txtLog.append( baudRate + "Rate" + "\n" );
			
			serialPort.setSerialPortParams( baudRate , SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			//CODE ON SETTING BAUD RATE ETC OMITTED
			//XBEE PAIR ASSUMED TO HAVE SAME SETTINGS ALREADY

			//enables the controls on the GUI if a successful connection is made
			window.keybindingController.toggleControls();
		}
		catch (PortInUseException e)
		{
			logText = selectedPort + " is in use. (" + e.toString() + ")";

			window.txtLog.setForeground(Color.RED);
			window.txtLog.append(logText + "\n");
		}
		catch (Exception e)
		{
			logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
			window.txtLog.append(logText + "\n");
			window.txtLog.setForeground(Color.RED);
		}
	}

	//open the input and output streams
	//pre: an open port
	//post: initialized input and output streams for use to communicate data
	public boolean initIOStream()
	{
		//return value for whether opening the streams is successful or not
		boolean successful = false;

		try {

			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();

			successful = true;
			return successful;
		}
		catch (IOException e) {
			logText = "I/O Streams failed to open. (" + e.toString() + ")";
			window.txtLog.setForeground(Color.red);
			window.txtLog.append(logText + "\n");
			return successful;
		}
	}

	//starts the event listener that knows whenever data is available to be read
	//pre: an open serial port
	//post: an event listener for the serial port that knows when data is received
	public void initListener()
	{
		try
		{
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		}
		catch (TooManyListenersException e)
		{
			logText = "Too many listeners. (" + e.toString() + ")";
			window.txtLog.setForeground(Color.red);
			window.txtLog.append(logText + "\n");
		}
	}

	//disconnect the serial port
	//pre: an open serial port
	//post: closed serial port
	public void disconnect()
	{
		//close the serial port
		try
		{
			//byte temp = 0;
			//writeData(temp);

			serialPort.removeEventListener();
			serialPort.close();
			input.close();
			output.close();
			
			setConnected(false);
			window.keybindingController.toggleControls();

			logText = "Disconnected.";
			window.txtLog.setForeground(Color.red);
			window.txtLog.append(logText + "\n");
		}
		catch (Exception e)
		{
			logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
			window.txtLog.setForeground(Color.red);
			window.txtLog.append(logText + "\n");
		}
	}

	final public boolean getConnected()
	{
		return bConnected;
	}

	public void setConnected(boolean bConnected)
	{
		this.bConnected = bConnected;
	}

	//what happens when data is received
	//pre: serial event is triggered
	//post: processing on the data it reads
	public void serialEvent(SerialPortEvent evt) {
		
		if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
		{
			try
			{
				//byte singleData = (byte)input.read();
				//String Data = new String(singleData);
				
				// ���ڿ� ���
				byte[] buffer = new byte[1024];
				
	            int len = -1;

	            try
	            {
	                while ( ( len = input.read(buffer)) > -1 )
	                {
	                    System.out.print(new String(buffer, 0, len));
	                    window.txtLog.append(new String(buffer,0,len));
	                }
	                
	                window.txtLog.append("\n");
	                System.out.print("\n");
	                
	            }
	            
	            catch ( IOException e )
	            {
	                e.printStackTrace();
	            }
	            
			}
			catch (Exception e)
			{
				logText = "Failed to read data. (" + e.toString() + ")";
				window.txtLog.setForeground(Color.red);
				window.txtLog.append(logText + "\n");
			}
			
		}
		
	}

	//method that can be called to send data
	//pre: open serial port
	//post: data sent to the other device
	public void writeData(byte Data)
	{
		try
		{
			output.write(Data);
			output.flush();
			
			System.out.println("Send Byte " + Data);
			window.txtLog.append("Send Byte " + Data + "\n");
		}
		catch (Exception e)
		{
			//logText = "Failed to write data. (" + e.toString() + ")";
			window.txtLog.setForeground(Color.red);
			window.txtLog.append(logText + "\n");
		}
	}

	public void updateRGB(byte rValue, byte gValue, byte bValue) {
		writeData(rValue);
		writeData(gValue);
		writeData(bValue);
		
	}

	public void updateFrame(byte[] rgbMap) {
		for (int i = 0; i < rgbMap.length; i++) {
			writeData(rgbMap[i]);
		}
		
	}

}

