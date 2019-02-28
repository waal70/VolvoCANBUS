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
		log.info("queue constructed");
	}
	/**
	 * @param c
	 */
	public CanMessageQueue(Collection<? extends CanMessage> c) {
		super(c);
	}

}
