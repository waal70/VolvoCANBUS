/**
 * 
 */
package org.waal70.canbus;

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
	
		
	public abstract CanId2 getCanId();
	public abstract String getHexCanId();
	
	public abstract String getMessage();
	
	public abstract String messageAsCommand();
	//01a#11223344AABBCCDD
	
	

}
