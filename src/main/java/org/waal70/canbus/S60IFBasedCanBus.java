/**
 * 
 */
package org.waal70.canbus;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.waal70.canbus.CanSocket.CanFrame;
import org.waal70.canbus.CanSocket.CanInterface;
import org.waal70.canbus.CanSocket.Mode;

/**
 * @author awaal This class represents the CanBus that is based on an actual
 *         interface. It uses sockets to connect to the interface and receives
 *         and writes messages using the appropriate methods. Once the received
 *         data is normalized in a CanMessage, it is enqueued onto the
 *         CanMessageQueue
 * 
 */
public class S60IFBasedCanBus implements CanBus {
	/**
	 * @param _cmq
	 */
	private static Logger log = Logger.getLogger(S60IFBasedCanBus.class);
	private static final String CAN_INTERFACE = "can0";
	private boolean _ISLISTENING = false;
	private CanSocket mySocket = new CanSocket(Mode.RAW);
	private CanMessageQueue _cmq;

	private CanInterface _canif;

	public boolean connect() {
		try {
			_canif = new CanInterface(mySocket, CAN_INTERFACE);
			mySocket.bind(_canif);
		} catch (IOException e) {
			log.error("IOException when opening socket. " + e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	public void close() {
		try {
			mySocket.close();
		} catch (IOException e) {
			log.error("Cannot close the socket to " + CAN_INTERFACE);
		}

	}

	public void listen() {
		_ISLISTENING = true;
		int i = 0;
		ExecutorService es = Executors.newCachedThreadPool();
		while (_ISLISTENING) {
			log.debug("Creating new FUTURE: sequence number is " + i++);
			Future<?> future = es.submit(new Callable<String>() {

				public String call() {
					try {
						CanFrame cf = mySocket.recv();
						CanMessage cm = new CanMessage();
						if (cm.parseReal(cf)) {
							_cmq.add(cm);
						}
						log.debug("Received message: " + cf.toString());
						return "OK";
					} catch (IOException e) {
						log.error("Exception on mySocket.recv() " + e.getLocalizedMessage());
						return "NOK";
					}

				}

			});
			try {
				// if I keep on getting OKs, I keep on receiving:
				if (future.get(5, TimeUnit.SECONDS) == "OK") {
					// The task has responded timely, going round for another one.
					log.debug("OK received, doing another round!");
				} else {
					// The task did complete within the timeout period, but the
					// response was something unexpected.
					log.error("mySocket.recv() did not time-out, but errored out. Aborting.");
					_ISLISTENING = false;
					future.cancel(true);
				}
			} catch (TimeoutException | InterruptedException | ExecutionException e) {
				log.debug("Timeout. _ISLISTENING to false " + e.getLocalizedMessage());
				_ISLISTENING = false;
				future.cancel(true);

			}
		} // end while islistening
		es.shutdownNow();
	}

	public void setLogisticsType(LogisticsType logisticsType) {
		// TODO Auto-generated method stub

	}

	public void setListenFilter(int iFilter) {
		// TODO Auto-generated method stub

	}

	public int getListenFilter() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void clearListenFilter() {
		// TODO Auto-generated method stub

	}

	public S60IFBasedCanBus(CanMessageQueue _cmq) {
		super();
		log.info("Instantiating an interface-based CanBus.");
		this._cmq = _cmq;
	}

}
