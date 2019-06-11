/**
 * 
 */
package org.waal70.canbus;

import org.apache.log4j.Logger;

/**
 * @author awaal
 *
 */
public class CanMessageQueueConsumer extends Thread {

	private String name;
	private static Logger log = Logger.getLogger(CanMessageQueueConsumer.class);
	private CanMessageQueue _cmq;

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
			CanMessage cm = _cmq.get();
			while (_cmq.isActive()) {
				if (cm != null)
					log.debug("Consumer " + this.name + " got: " + cm.getCanMessage());
				Thread.sleep(100);
				cm = _cmq.get();
				if (cm != null)
					log.debug("Consumer " + this.name + " got: " + cm.getCanMessage());

			}
			log.debug("Finished processing. " + this.name + " terminating.");

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}
}
