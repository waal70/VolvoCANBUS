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
	@Override
	public void run() {
		log.debug("run() called for thread " + _threadName);
		_scb.connect();
		_scb.listenReal();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt() {
		if (this.isInterrupted())
			log.debug("Interrupt called and thread is now interrupted");
		_scb.close();
		log.debug("closed queue. bye now");
		//super.interrupt();
		
	}

}
