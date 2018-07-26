/**
 * 
 */
package org.waal70.canbus;

import org.apache.log4j.Logger;

/**
 * @author awaal
 * This is the Runnable CANBUS reader
 * It will connect to the representation of the Canbus
 *
 */
public class S60CanBusReader implements Runnable {
	private S60CanBus _scb;
	private String _threadName;
	private static Logger log = Logger.getLogger(S60CanBusReader.class);

	/**
	 * 
	 */
	public S60CanBusReader(String threadName) {
		log.debug("Thread started, its name is " + threadName);
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
		_scb.listen();
	}

}
