/**
 * 
 */
package org.waal70.canbus;

import org.apache.log4j.Logger;
import org.waal70.canbus.CanBus.CanBusType;

/**
 * @author awaal
 * This is the  CANBUS reader
 * It will connect to the representation of the Canbus
 * and queue messages it receives
 *
 */
public class S60CanBusReader extends Thread{
	private CanBus _scb;
	private String _threadName;
	private static Logger log = Logger.getLogger(S60CanBusReader.class);

	/**
	 * 
	 */
	public S60CanBusReader(String threadName) {
		log.debug("CanbusReaderThread started, its name is " + threadName);
		_threadName = threadName;
		log.info ("Creating a reader based on the set type: " + VolvoCANBUS.CANBUSMODE);
		
		if (VolvoCANBUS.CANBUSMODE == CanBusType.FILEBASED)
			_scb = new S60FileBasedCanBus(CanMessageQueue.getInstance());
		else
			_scb = new S60IFBasedCanBus(CanMessageQueue.getInstance());

	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		log.debug("run() called for thread " + _threadName);
		_scb.connect();
		_scb.listen();
		// if we are returning from the listen-function, it means
		// that we need to stop running!
		this.interrupt();
		log.debug("end of run()");
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt() {
		log.debug("Interrupt called. Thread " + _threadName + " is signing off.");
		_scb = null;
		super.interrupt();
	}



}
