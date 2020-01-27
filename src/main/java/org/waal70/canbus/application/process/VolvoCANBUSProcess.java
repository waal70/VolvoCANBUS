/**
 * 
 */
package org.waal70.canbus.application.process;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.waal70.canbus.features.produce.S60CanBusReader;

/**
 * @author awaal
 *
 */
public class VolvoCANBUSProcess{
	
	public static void doCanbusAlt() throws Exception
	{
		ExecutorService threadPool = Executors.newFixedThreadPool(3);
		//First, instantiate the consumers:
		//threadPool.execute(new CanMessageQueueConsumer("1"));
		//threadPool.execute(new CanMessageQueueConsumer("2"));
		//Then, the producer
		Future<?> producerStatus = threadPool.submit(new S60CanBusReader("Producer one"));
		// Now, kith
		producerStatus.get();
		threadPool.shutdown();
		
	}

}
