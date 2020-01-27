/**
 * 
 */
package org.waal70.canbus.features.consume;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.waal70.canbus.features.queue.CanMessage;
import org.waal70.canbus.features.queue.CanMessageQueue;

/**
 * @author awaal
 * The QueueConsumer is a threaded implementation of the queue
 * reader. It takes the CanMessageQueue and keeps on reading from
 * it untill the queue is empty and no further messages are to be
 * expected
 *
 */
public class CanMessageQueueConsumer extends Thread {

	private String name;
	private static Logger log = LogManager.getLogger(CanMessageQueueConsumer.class);
	private CanMessageQueue _cmq;
	private int msgCounter = 0;

	/**
	 * @param name
	 * Sets the name for this consumer
	 */
	public CanMessageQueueConsumer(String name) {
		super(name);
		this.name = name;
		this._cmq = CanMessageQueue.getInstance();
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
