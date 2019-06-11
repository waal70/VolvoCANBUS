/**
 * 
 */
package org.waal70.canbus.features.queue;

import org.waal70.canbus.CanSocket;
import org.waal70.canbus.CanSocket.CanId;

/**
 * @author awaal
 * This class serves as a generic CanBusMessage
 */
public abstract class CanBusMessage {
	
	public int id;
	public boolean rtr;
	public boolean ext;
	public boolean err;
	public int flags;
	
		
	public abstract CanId getCanId();
	public abstract String getHexCanId();
	
	public abstract String getMessage();
	
	public abstract String messageAsCommand();
	//01a#11223344AABBCCDD
	
	

}
