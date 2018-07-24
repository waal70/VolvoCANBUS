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
 * This is the specific implementation
 * The interface for the program should be CanMessageQueue
 * This is the provider that will fill the CanMessageQueue
 */
public class S60CanBus implements CanBus{
	/**
	 * @param _cmq
	 */
	private static Logger log = Logger.getLogger(S60CanBus.class);
	private static final boolean FAKE_IT = true;
	private CanMessageQueue _cmq;
	private ProcessBuilder _pb = new ProcessBuilder("/Users/awaal/cansend");
	private Process _p;
	private BufferedReader _br; 
	private int _queueLength;
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
	public void listen() {
		// TODO Auto-generated method stub
		
	}
	public CanBusMessage dequeue() {
		// TODO Auto-generated method stub
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
		
		return _cmq.dequeue();
	}
	public void setLogisticsType(LogisticsType logisticsType) {
		// TODO Auto-generated method stub
		
	}
	public void setQueueLength(int qLength) {
		_queueLength = qLength;
		_cmq.setQueueLength(qLength);
		
	}
	public int getQueueLength() {
		return _queueLength;
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

}
