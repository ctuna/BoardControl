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
import java.util.Calendar;
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
	private static final int DEFAULT_DELAY = 100; //in milliseconds
	private long lastPressTime = Calendar.getInstance().getTimeInMillis();

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
	int prevKeyPressVal = 0;
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		int center_threshold = 10;
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=null;
                if (input.ready()) {
                    inputLine = input.readLine();
                }
				Double inputVal = Double.parseDouble(inputLine);
				System.out.println(inputLine);
				long curTime = Calendar.getInstance().getTimeInMillis();
				if (Math.abs(inputVal)<= 90){
					//if (curTime - lastPressTime >= 
					//		pulseWidth(Math.abs(inputVal))) {
						lastPressTime = curTime;
						if (Math.abs(inputVal)<center_threshold){
							//CENTER
							robo.keyRelease(KeyEvent.VK_LEFT);
							robo.keyRelease(KeyEvent.VK_RIGHT);
						}
						else if (inputVal>0){
							robo.keyPress(KeyEvent.VK_RIGHT);
							//robo.keyRelease(KeyEvent.VK_RIGHT);
						}
						else {
							robo.keyPress(KeyEvent.VK_LEFT);
							//robo.keyRelease(KeyEvent.VK_LEFT);
						}
					//System.out.println(inputVal);
					//prevKeyPressVal = inputVal;
					//}
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	private int pulseWidth(int angle) {
		double a = angle*1.0;
		if (angle == 0)
			return DEFAULT_DELAY;
		return (int) (1.0 / angle * DEFAULT_DELAY);
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