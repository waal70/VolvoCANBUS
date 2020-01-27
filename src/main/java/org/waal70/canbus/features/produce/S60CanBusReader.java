/**
 * 
 */
package org.waal70.canbus.features.produce;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.waal70.canbus.features.queue.CanBus;

/**
 * @author awaal This is the Workerthread for the reading the canbus Based on
 *         the required type, it will instantiate a concrete CanBus, connect to
 *         it, and make the concrete CanBus listen.
 *         In fact, even through it is called a Reader (because it reads the
 *         physical CanBus), for the scope of this JAVA program, it is considered
 *         a Producer
 */

public class S60CanBusReader extends Thread {
	private CanBus _scb;
	private String _threadName;
	private static Logger log = LogManager.getLogger(S60CanBusReader.class);

	/**
	 * 
	 */
	public S60CanBusReader(String threadName) {
		log.debug("CanbusReaderThread started, its name is " + threadName);
		_threadName = threadName;
		_scb = CanBusFactory.getCanBus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		log.debug("run() called for thread " + _threadName);
		_scb.connect();
		_scb.addListenFilter(null);
		_scb.listen();
		// if we are returning from the listen-function, it means
		// that we need to stop running!
		_scb.setInactive();
		this.interrupt();
		log.debug("end of run()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt() {
		log.debug("Interrupt called. Thread " + _threadName + " is signing off.");
		_scb = null;
		super.interrupt();
	}

}
