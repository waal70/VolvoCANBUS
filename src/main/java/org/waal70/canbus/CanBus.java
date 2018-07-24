/**
 * 
 */
package org.waal70.canbus;

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
	public boolean connect();
	public void close();
	public void listen();
	public CanBusMessage dequeue();
	public void setLogisticsType(CanBus.LogisticsType logisticsType);
	public void setQueueLength(int qLength);
	public int getQueueLength();
	public void setListenFilter(int iFilter);
	public int getListenFilter();
	public void clearListenFilter();
	
	

}
