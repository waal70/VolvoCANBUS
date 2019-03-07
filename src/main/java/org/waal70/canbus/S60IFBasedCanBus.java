/**
 * 
 */
package org.waal70.canbus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import org.waal70.canbus.util.net.ProbeInterface;

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
	private static final String CAN_INTERFACE = VolvoCANBUS.prop.getProperty("VolvoCANBUS.ifname","can0");
	private boolean _ISLISTENING = false;
	private CanSocket mySocket = new CanSocket(Mode.RAW);
	private CanMessageQueue _cmq;
	
//	private final Set<CanFilter> filters = new HashSet<>();
//	private CanFilter[] filterArray = new CanFilter[0];

	private CanInterface _canif;

	public boolean connect() {
		if (!ProbeInterface.confirmCanBusPresentAndActive())
			if (!bringIFUp()) return false;
		
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
		log.debug(CAN_INTERFACE);
		ExecutorService es = Executors.newCachedThreadPool();
		while (_ISLISTENING) {
			log.debug("Creating new FUTURE: sequence number is " + i++);
			Future<?> future = es.submit(new Callable<String>() {

				public String call() {
					try {
						//CanId filterid = new CanId(123456);
						//filterid.setEFFSFF();
						//CanFilter filter1 = new CanFilter(filterid, -1);
												
						//log.debug("Filter match? : " + filter1.matchId(123456));
						//CanFilter[] filterArray = {filter1};
						//mySocket.setFilters(filterArray);
						//mySocket.setFilters(data);
						CanFrame cf = mySocket.recv();
						mySocket.getFilters();
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
	private boolean bringIFUp()
	{
		log.debug("Trying to bring CAN up...");
		ArrayList<String> cmd = new ArrayList<String>();
		cmd.add("sudo");
		cmd.add(VolvoCANBUS.prop.getProperty("VolvoCANBUS.ip"));
		cmd.add("link");
		cmd.add("set");
		cmd.add(VolvoCANBUS.prop.getProperty("VolvoCANBUS.ifname",CAN_INTERFACE));
		cmd.add("up");
		cmd.add("type");
		cmd.add("can");
		cmd.add("bitrate");
		cmd.add(VolvoCANBUS.prop.getProperty("VolvoCANBUS.bitrate"));
		cmd.add("loopback");
		if (VolvoCANBUS.prop.getProperty("VolvoCANBUS.loopback").equalsIgnoreCase("YES"))
		{
			cmd.add("on");
			log.debug("loopback set to ON");
		}
		else
		{
			cmd.add("off");
			log.debug("loopback set to OFF");
		}
		
		ProcessBuilder pb = new ProcessBuilder(cmd);
		Process p;
		BufferedReader br;
		try {
			p = pb.start();
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			log.info("Feedback is (null is good): " + br.readLine());
			br.close();
			p.destroy();
		} catch (IOException  e) {
			log.error("Cannot bring CAN UP: " + e.getLocalizedMessage());
			return false;
		}
		return true;
		
	}

}
