/**
 * 
 */
package org.waal70.canbus;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.waal70.canbus.CanSocket.CanFrame;
import org.waal70.canbus.CanSocket.CanId;
import org.waal70.canbus.CanSocket.CanInterface;
import org.waal70.canbus.CanSocket.Mode;
import org.waal70.canbus.util.net.ProbeInterface;

/**
 * @author awaal
 * This is the main class
 */
public class VolvoCANBUS {
	private static Logger log = Logger.getLogger(VolvoCANBUS.class);
	

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		initLog4J();
		log.info("Program start.");
		//Instantiate two bus-readers. Just for the heck of it :)
		
		ExecutorService es = Executors.newCachedThreadPool();
		es.execute(new S60CanBusReader("Reader een"));
		es.execute(new S60CanBusReader("Reader twee"));
		

		TimeUnit.SECONDS.sleep(5);
	      try (final CanSocket socket = new CanSocket(Mode.RAW)) {
	            final CanInterface canif = new CanInterface(socket, "can0");
	            socket.bind(canif);
	            socket.setLoopbackMode(true);
	            socket.send(new CanFrame(canif,
	                    new CanId(0x5), new byte[] {0,0,0,0,0,0,0,0}));
	      }
	      catch (Exception e)
	      {
	    	  log.error("Oops");
	      }
		
		es.shutdownNow();
		es.awaitTermination(1000, TimeUnit.MILLISECONDS);



	
	
		log.info(CanMessageQueue.getInstance().poll().messageAsCommand());
		if (!ProbeInterface.findInterface("can0"))
			log.error("No CAN interface found!");
		//ProbeInterface.listInterfaces();
		//log.info(ProbeInterface.listInterfaces());
			
			
		//pi.listInterfaces();
		
	 	 
		 //cm.setCanId("12312312");
		 //the extended can-id has 4 bytes (actually 29 bits, but who cares)
		 // the regular can-id has 3 characters (11 bits, but who cares)

		 // filter on can-id 12312312:
		 //./candump can0,12312312:1fffffff
		 //
		 // messages arrive as (HEX encoded):
		 //  can0  12312312   [8]  11 22 33 44 55 66 77 88
		log.info("Reached end of main-thread.");


	}
	private static void initLog4J()
	{
		InputStream is = VolvoCANBUS.class.getResourceAsStream("/log4j.properties");
		PropertyConfigurator.configure(is);
	}

}
