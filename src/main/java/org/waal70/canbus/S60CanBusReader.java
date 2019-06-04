/**
 * 
 */
package org.waal70.canbus;

import org.apache.log4j.Logger;

/**
 * @author awaal This is the Workerthread for the reading the canbus Based on
 *         the required type, it will instantiate a concrete CanBus, connect to
 *         it, and make the concrete CanBus listen.
 *
 */
public class S60CanBusReader extends Thread {
	private CanBus _scb;
	private String _threadName;
	private static Logger log = Logger.getLogger(S60CanBusReader.class);

	/**
	 * 
	 */
	public S60CanBusReader(String threadName) {
		log.debug("CanbusReaderThread started, its name is " + threadName);
		_threadName = threadName;
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("mac")) {
			// Operating system is Apple OSX based
			VolvoCANBUS.prop.put("VolvoCANBUS.CanBusType", "FILEBASED");
			VolvoCANBUS.prop.put("VolvoCANBUS.SendProcess" , VolvoCANBUS.prop.getProperty("VolvoCANBUS.SendProcessMAC"));
			log.info("Detected Apple OSX. Going to override your choice for CanBusType.");
		} //else if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
		//	_override = false;
		//}
		log.info("Creating a reader based on the set type: " + VolvoCANBUS.prop.getProperty("VolvoCANBUS.CanBusType"));

			if (VolvoCANBUS.prop.getProperty("VolvoCANBUS.CanBusType").equalsIgnoreCase("FILEBASED"))
				_scb = new S60FileBasedCanBus(CanMessageQueue.getInstance());
			else
				_scb = new S60IFBasedCanBus(CanMessageQueue.getInstance());
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
