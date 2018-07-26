/**
 * 
 */
package org.waal70.canbus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * @author awaal
 * This class represents the CanBus as a writeLine() separated
 * stream of messages that have the format:
 * can0 12312312 [8] 11 22 33 44 55 66 77 88
 * can0 12312313 [8] 22 33 44 55 66 77 88 99
 * can0 12312314 [8] 33 44 55 66 77 88 99 AA
 * They will be parsed and put onto the CanMessageQueue
 */
public class S60CanBus implements CanBus{
	/**
	 * @param _cmq
	 */
	private static Logger log = Logger.getLogger(S60CanBus.class);
	private static final boolean FAKE_IT = true;
	private boolean _ISLISTENING = false;
	private CanMessageQueue _cmq;
	private ProcessBuilder _pb = new ProcessBuilder("/Users/awaal/cansend");
	private Process _p;
	private BufferedReader _br; 
	//should implement "connect" or "open"
	// send (message)
	// listenFilter (to a can-id)
	// listen (creating an array of received messages, maxing out at 255?)
	public boolean connect() {
		log.debug("connecting to bus...");
		if (FAKE_IT)
		{
			try {
				_p = _pb.start();
				_br = new BufferedReader(new InputStreamReader(_p.getInputStream()));
			} catch (IOException e) {
				log.error("Kan process niet starten");
			}
			return true;
		}
		return true;

	}
	public void close() {
		if (_pb != null) _pb = null;

		if (_br != null)
			try {
				_br.close();
			} catch (IOException e) {
				log.error("Cannot close buffered reader. Setting to null");
				_br = null;
			}
		if (_p != null) _p.destroyForcibly();
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
		_ISLISTENING=true;
		try {
			for (String line = _br.readLine(); line != null; line = _br.readLine()) {
				CanMessage cm = new CanMessage();
				if (cm.parseMessage(line)) 
				{
					_cmq.add(cm);
					//log.debug("Message from parsed line: " + cm.messageAsCommand());
				}
			}
		}
		catch (IOException e)
		{
			log.error("Fout");
		}

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
