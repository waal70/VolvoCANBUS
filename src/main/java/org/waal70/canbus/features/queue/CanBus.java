/**
 * 
 */
package org.waal70.canbus.features.queue;

import org.waal70.canbus.CanSocket;
import org.waal70.canbus.CanSocket.CanFilter;

/**
 * @author awaal
 *
 */
public interface CanBus {
	//should implement "connect" or "open"
	// send (message)
	// listenFilter (to a can-id)
	// listen (creating an array of received messages, maxing out at 255?)
	enum LogisticsType {
		FIFO,
		LIFO,
		RANDOM
	}
	public void setInactive();
	public boolean connect();
	public void close();
	public void listen();
	public void setLogisticsType(CanBus.LogisticsType logisticsType);
	/**
	 * This method sets one filter. Use addFilter to add multiple filters
	 * @param setFilter
	 */
	public void setListenFilter(CanFilter setFilter);
	/**
	 * This method appends a filter to the filter array
	 * and subsequently sets it on the socket.
	 * @param addFilter contains the filter to add
	 */
	public void addListenFilter(CanFilter addFilter);
	public CanFilter[] getListenFilter();
	public void clearListenFilter();
	
	

}
