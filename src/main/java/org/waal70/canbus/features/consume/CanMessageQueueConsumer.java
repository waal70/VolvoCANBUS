/**
 * 
 */
package org.waal70.canbus.features.consume;

import org.apache.log4j.Logger;
import org.waal70.canbus.features.queue.CanMessage;
import org.waal70.canbus.features.queue.CanMessageQueue;

/**
 * @author awaal
 *
 */
public class CanMessageQueueConsumer extends Thread {

	private String name;
	private static Logger log = Logger.getLogger(CanMessageQueueConsumer.class);
	private CanMessageQueue _cmq;
	private int msgCounter = 0;

	/**
	 * 
	 */
	public CanMessageQueueConsumer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param target
	 */
	public CanMessageQueueConsumer(Runnable target) {
		super(target);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public CanMessageQueueConsumer(String name) {
		super(name);
		this.name = name;
		this._cmq = CanMessageQueue.getInstance();
	}

	/**
	 * @param group
	 * @param target
	 */
	public CanMessageQueueConsumer(ThreadGroup group, Runnable target) {
		super(group, target);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param name
	 */
	public CanMessageQueueConsumer(ThreadGroup group, String name) {
		super(group, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param target
	 * @param name
	 */
	public CanMessageQueueConsumer(Runnable target, String name) {
		super(target, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param target
	 * @param name
	 */
	public CanMessageQueueConsumer(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param target
	 * @param name
	 * @param stackSize
	 */
	public CanMessageQueueConsumer(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, target, name, stackSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		log.debug("Running now for " + this.name);
		try {
			CanMessage cm = null; //= _cmq.get();
			
			// As long as the queue is signalling it is still active,
			// we can expect more messages, therefore, continue the while loop
			// Should the queue signal non-active, but it is not empty yet, 
			// also continue
			while (_cmq.isActive() || !_cmq.isEmpty()) {
				cm = _cmq.get();
				if (cm != null)
				{
					log.info("Consumer " + this.name + " got: " + cm.getCanMessage());
					msgCounter++;
				}
				Thread.sleep(100);
			}
			log.debug("Finished processing. " + this.name + "(" + msgCounter + ") terminating.");

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}
}
