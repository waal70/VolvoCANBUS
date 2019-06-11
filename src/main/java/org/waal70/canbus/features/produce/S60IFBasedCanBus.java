/**
 * 
 */
package org.waal70.canbus.features.produce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.waal70.canbus.CanSocket;
import org.waal70.canbus.CanSocket.CanFilter;
import org.waal70.canbus.CanSocket.CanFrame;
import org.waal70.canbus.CanSocket.CanId;
import org.waal70.canbus.CanSocket.CanInterface;
import org.waal70.canbus.CanSocket.Mode;
import org.waal70.canbus.application.VolvoCANBUS;
import org.waal70.canbus.features.queue.CanBus;
import org.waal70.canbus.features.queue.CanMessage;
import org.waal70.canbus.features.queue.CanMessageQueue;
import org.waal70.canbus.util.ProbeInterface;

/**
 * @author awaal This class represents the CanBus that is based on an actual
 *         interface. It uses sockets to connect to the interface and receives
 *         and writes messages using the appropriate methods. Once the received
 *         data is normalized in a CanMessage, it is enqueued onto the
 *         CanMessageQueue, making this the Producer in the
 *         Consumer/Producer pattern
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
	private List<CanFilter> _filterArray = new ArrayList<CanFilter>(); 
	
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
				//e.printStackTrace();
				_ISLISTENING = false;
				future.cancel(true);

			}
		} // end while islistening
		es.shutdownNow();
	}

	public void setLogisticsType(LogisticsType logisticsType) {
		log.info(Thread.currentThread().getStackTrace()[1].getMethodName() + "() not implemented for interface-based Canbus");

	}
	/* (non-Javadoc)
	 * @see org.waal70.canbus.CanBus#addListenFilter(org.waal70.canbus.CanSocket.CanFilter)
	 * add filter takes the array of currently assigned filters and adds one to the collection
	 * It then proceeds to set the new collection.
	 */
	@Override
	public void addListenFilter(CanFilter addFilter) {
		if (addFilter == null)
			addFilter = CanFilter.ANY;
		log.debug("addListenFilter called with filter: " + addFilter.getIdHex());
		//setListenFilter(addFilter);
		if (_filterArray != null)
			_filterArray.add(addFilter);
		else
			log.error("filterArray somehow is null!");
		
		//even testen:
		CanId f1id = new CanId(0x12345678);
		f1id.setEFFSFF();
		CanId f2id = new CanId(0x87654321);
		f2id.setEFFSFF();
		CanFilter f1 = new CanFilter(f1id,0xDFFFFFFF);
		CanFilter f2 = new CanFilter(f2id,0xDFFFFFFF);
		_filterArray.clear();
		_filterArray.add(f1);
		_filterArray.add(f2);
		
		setFilter();
		
	}

	/* (non-Javadoc)
	 * @see org.waal70.canbus.CanBus#setListenFilter(org.waal70.canbus.CanSocket.CanFilter)
	 * This method sets exactly one filter, it therefore clears all other filters and
	 * then uses the iFilter parameter to set the one filter.
	 */
	public void setListenFilter(CanFilter iFilter) {
		
		if (iFilter == null)
			iFilter = CanFilter.ANY;
		
		log.debug("setListenFilter entered with filter: " + iFilter.getIdHex());
		
		if (!_filterArray.isEmpty())
		{
			_filterArray.clear();
			clearListenFilter();
		}
		
		_filterArray.add(iFilter);

		setFilter();
	}
	
	private boolean setFilter() {
		log.debug("setFilter called, going to apply "+_filterArray.size() + " filters.");	
		
		if (_filterArray.isEmpty())
		{
			//filterArray has no elements, there is therefore no filter to set.
			//I assume this means clearing it, so here we go:
			setListenFilter(CanFilter.ANY);
			return true;
		}
		
		//Triple whammy on the Array type :)
		CanFilter[] filterArray = ((List<CanFilter>)_filterArray).toArray(new CanFilter[_filterArray.size()]);
		mySocket.setFilters(filterArray);
		log.debug("getFilters: ");
		mySocket.getFilters();
		return true;
		
	}

	public CanFilter[] getListenFilter() {
	
		return null;
	}

	public void clearListenFilter() {
		mySocket.setFilters(CanFilter.ANY);

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

	@Override
	public void setInactive() {
		this._cmq.setInactive();
	
	}

}