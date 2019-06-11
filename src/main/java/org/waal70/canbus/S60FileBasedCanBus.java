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
import org.waal70.canbus.CanSocket.CanFilter;

/**
 * @author awaal
 *
 */
public class S60FileBasedCanBus implements CanBus {
	private static Logger log = Logger.getLogger(S60FileBasedCanBus.class);
	private ProcessBuilder _pb = new ProcessBuilder(VolvoCANBUS.prop.getProperty("VolvoCANBUS.SendProcess"));
	private Process _p;
	private BufferedReader _br;
	private CanMessageQueue _cmq;

	/**
	 * 
	 */
	public S60FileBasedCanBus(CanMessageQueue _cmq) {
		super();
		log.info("Instantiating a FileBased Canbus.");
		this._cmq = _cmq;
	}

	/* (non-Javadoc)
	 * @see org.waal70.canbus.CanBus#connect()
	 */
	@Override
	public boolean connect() {
			try {
				_p = _pb.start();
				_br = new BufferedReader(new InputStreamReader(_p.getInputStream()));
			} catch (IOException e) {
				log.error("Cannot start: " + e.getLocalizedMessage());
			}
			return true;
		
	}

	/* (non-Javadoc)
	 * @see org.waal70.canbus.CanBus#close()
	 */
	@Override
	public void close() {
		if (_pb != null)
			_pb = null;

		if (_br != null)
			try {
				_br.close();
			} catch (IOException e) {
				log.error("Cannot close buffered reader. Setting to null.");
				_br = null;
			}
		if (_p != null)
			_p.destroyForcibly();
		log.debug("File-based CanBus closed.");


	}

	/* (non-Javadoc)
	 * @see org.waal70.canbus.CanBus#listen()
	 */
	@Override
	public void listen() {

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
			log.debug("Timeout on readLine(). Quitting anyway.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		es.shutdownNow();
		this.setInactive();
		return;

	}
	/* (non-Javadoc)
	 * @see org.waal70.canbus.CanBus#setLogisticsType(org.waal70.canbus.CanBus.LogisticsType)
	 */
	@Override
	public void setLogisticsType(LogisticsType logisticsType) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.waal70.canbus.CanBus#getListenFilter()
	 */
	@Override
	public CanFilter[] getListenFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.waal70.canbus.CanBus#clearListenFilter()
	 */
	@Override
	public void clearListenFilter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListenFilter(CanFilter setFilter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListenFilter(CanFilter addFilter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInactive() {
		this._cmq.setInactive();
		
	}


}
