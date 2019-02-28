/**
 * 
 */
package org.waal70.canbus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
 * @author awaal This class represents the CanBus as a writeLine() separated
 *         stream of messages that have the format: can0 12312312 [8] 11 22 33
 *         44 55 66 77 88 can0 12312313 [8] 22 33 44 55 66 77 88 99 can0
 *         12312314 [8] 33 44 55 66 77 88 99 AA They will be parsed and put onto
 *         the CanMessageQueue
 */
public class S60CanBus implements CanBus {
	/**
	 * @param _cmq
	 */
	private static Logger log = Logger.getLogger(S60CanBus.class);
	private static final boolean FAKE_IT = false;
	private static final String CAN_INTERFACE = "can0";
	private static final int CAN_SFF_MASK = 0x000007FF;
	private boolean _ISLISTENING = false;
	public long lastReceived = System.currentTimeMillis();
	private CanSocket mySocket = new CanSocket(Mode.RAW);
	private CanMessageQueue _cmq;
	private ProcessBuilder _pb = new ProcessBuilder("/Users/awaal/cansend");
	// private ProcessBuilder _pb = new
	// ProcessBuilder("/home/awaal/cansend","can0");
	private Process _p;
	private BufferedReader _br;

	// private CanSocket _socket;
	private CanInterface _canif;

	// should implement "connect" or "open"
	// send (message)
	// listenFilter (to a can-id)
	// listen (creating an array of received messages, maxing out at 255?)
	public boolean connect() {
		if (FAKE_IT) {
			return connectFake();
		} else
			return connectReal();
	}

	private boolean connectReal() {
		log.debug("Connecting to bus...(REAL)");
		try {
			_canif = new CanInterface(mySocket, CAN_INTERFACE);
			mySocket.bind(_canif);
		} catch (IOException e) {
			log.error("IOException when opening socket. " + e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	private boolean connectFake() {
		log.debug("Connecting to bus...(FAKING)");
		log.debug("PB: " + _pb.toString());
		if (FAKE_IT) {
			try {
				_p = _pb.start();
				_br = new BufferedReader(new InputStreamReader(_p.getInputStream()));
			} catch (IOException e) {
				log.error("Kan process niet starten: " + e.getLocalizedMessage());
			}
			return true;
		}
		return true;

	}

	public void close() {
		if (_pb != null)
			_pb = null;

		if (_br != null)
			try {
				_br.close();
			} catch (IOException e) {
				log.error("Cannot close buffered reader. Setting to null");
				_br = null;
			}
		if (_p != null)
			_p.destroyForcibly();
		log.debug("CanBus closed.");

	}

	public CanBusMessage dequeue() {
		if (!_ISLISTENING)
			listen();
		return _cmq.poll();
	}

	public CanMessageQueue getQueue() {
		if (!(_cmq == null))
			return _cmq;
		else
			return null;
	}

	public void listen() {
		_ISLISTENING = true;

		ExecutorService es = Executors.newSingleThreadExecutor();
		Future<?> future = es.submit(new Callable<Object>() {

			public String call() {
				try {
					for (String line = _br.readLine(); line != null; line = _br.readLine()) {
						CanMessage cm = new CanMessage();
						if (cm.parseMessage(line)) 
							_cmq.add(cm);
					}
				} catch (IOException e) {
					log.error("Fout bij readLine(): " + e.getLocalizedMessage());
				}
				return "OK";
			}

		});
		try {
			log.debug("Timing (final) call to 2 seconds: " + future.get(2, TimeUnit.SECONDS));
		} catch (TimeoutException e) {
			log.debug("Timeout");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		es.shutdownNow();
		return;

	}

	public void listenReal() {
		_ISLISTENING = true;
		log.debug("Socket options: " + CanSocket.getCanRawFilter());
		int canId = 2046;
		log.debug("filter is: " + canId);
		
		try {
			mySocket.setSocketOptions(canId);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
							lastReceived = System.currentTimeMillis();
						}
						log.debug("Received message: " + cf.toString());
						return "OK";
					} catch (IOException e) {
						log.error("Exception on mySocket.recv()" + e.getLocalizedMessage());
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

	public S60CanBus(CanMessageQueue _cmq) {
		super();
		this._cmq = _cmq;
	}

	@Override
	public int getQueueLength() {
		return _cmq.size();
	}

}
