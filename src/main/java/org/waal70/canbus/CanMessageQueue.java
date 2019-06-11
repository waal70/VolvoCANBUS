/**
 * 
 */
package org.waal70.canbus;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;
/**
 * @author awaal
 * @param <CanMessage>
 *
 */

public class CanMessageQueue extends ConcurrentLinkedQueue<CanMessage> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1302059239381608721L;
	private static Logger log = Logger.getLogger(CanMessageQueue.class);
	// Here is the implementation of the singleton pattern
	// as the queue should be central to the application 
	private static Boolean _isActive = Boolean.TRUE;
	
	
	private static class SingletonHelper{
        private static final CanMessageQueue INSTANCE = new CanMessageQueue();
    }
    public static CanMessageQueue getInstance(){
        return SingletonHelper.INSTANCE;
    }

	/**
	 * 
	 */
	private CanMessageQueue() {
		super();
		log.info("CanMessageQueue constructed.");
	}
	/**
	 * @param c
	 */
	public CanMessageQueue(Collection<? extends CanMessage> c) {
		super(c);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ConcurrentLinkedQueue#add(java.lang.Object)
	 */
	@Override
	public boolean add(CanMessage e) {
		log.debug("CanMessageQueue add. Size now: " + (this.size() + 1));
		return super.add(e);
	}
	public void setInactive() {
		_isActive = Boolean.FALSE;
	}
	
	public boolean isActive()
	{
		return _isActive;
	}
	
	public CanMessage get() {
		//This method returns a CanMessage
		log.debug("CanMessageQueue get requested.");

		return this.poll();
		
	}

}
