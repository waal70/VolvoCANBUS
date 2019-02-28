/**
 * 
 */
package org.waal70.canbus;

import org.apache.log4j.Logger;

/**
 * @author awaal
 * This is the  CANBUS reader
 * It will connect to the representation of the Canbus
 * and queue messages it receives
 *
 */
public class S60CanBusReader extends Thread{
	private S60CanBus _scb;
	private String _threadName;
	private static Logger log = Logger.getLogger(S60CanBusReader.class);

	/**
	 * 
	 */
	public S60CanBusReader(String threadName) {
		log.debug("CanbusReaderThread started, its name is " + threadName);
		_threadName = threadName;
		_scb = new S60CanBus(CanMessageQueue.getInstance());

	}

	public CanMessageQueue getCanMessageQueue()
	{
		return _scb.getQueue();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		log.debug("run() called for thread " + _threadName);
		_scb.connect();
		_scb.listenReal();
		// if we are returning from the listen-function, it means
		// that we need to stop running!
		this.interrupt();
		//_scb.listen();
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
