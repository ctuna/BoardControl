import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;


public class Surfing implements SerialPortEventListener {
	SerialPort serialPort;
	Robot robo;
        /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-AM01QMOV", // Mac OS X
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
	};
	public Surfing(){
		super();
		try {
			robo = new Robot();
			robo.setAutoDelay(0);
			robo.setAutoWaitForIdle(true);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void initialize() {
	
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	// left and right swapped for snowboard and surf
	final int LEFT = 0;
	final int RIGHT = 1;
	final int MIDDLE = 2;
	final int JUMP = 3;
	int curPress = -1;
	
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				int inputVal = Integer.parseInt(inputLine);
				switch (inputVal){
					case (LEFT):
						if (curPress != KeyEvent.VK_LEFT && curPress != -1) {
							robo.keyRelease(curPress);
						}
						robo.keyPress(KeyEvent.VK_LEFT);
						//robo.delay(25);
						//robo.keyRelease(KeyEvent.VK_RIGHT);
						curPress = KeyEvent.VK_LEFT;
						break;
					case (RIGHT):
						if (curPress != KeyEvent.VK_RIGHT && curPress != -1) {
							robo.keyRelease(curPress);
						}
						robo.keyPress(KeyEvent.VK_RIGHT);
						//robo.delay(25);
						//robo.keyRelease(KeyEvent.VK_LEFT);
						curPress = KeyEvent.VK_RIGHT;
						break;
					case (MIDDLE):
						if (curPress != -1) {
							robo.keyRelease(curPress);
							curPress = -1;
						}
						break;
					case (JUMP):
						robo.keyPress(KeyEvent.VK_SPACE);
						robo.delay(25);
						robo.keyRelease(KeyEvent.VK_SPACE);
					default:
				}
				
				}
			 catch (Exception e) {
			//	System.err.println(e.toString());
			}
		}
		
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	public static void main(String[] args) throws Exception {
		Surfing main = new Surfing();
		
		main.initialize();
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started");
	}
}