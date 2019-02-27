/**
 * 
 */
package org.waal70.canbus;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.waal70.canbus.CanSocket.CanFrame;
import org.waal70.canbus.CanSocket.CanId;
import org.waal70.canbus.CanSocket.CanInterface;
import org.waal70.canbus.CanSocket.Mode;

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
		//Because of the nature of CANBUS (not a queue),
		// I need a single canbuslistener that will fill up a queue.
		
		S60CanBusReader scbr = new S60CanBusReader("Single Reader");
		scbr.start();
		canBusWrite();
		
		log.info("SEND MESSAGE NOW!");
		 
		 TimeUnit.SECONDS.sleep(5);
		 scbr.interrupt();
		 log.debug("waiting for thread to die");
		 scbr.join(1000);

		log.info("Reached end of main-thread.");


	}
	private static void canBusWrite()
	{
		try (final CanSocket socket = new CanSocket(Mode.RAW)) {
            final CanInterface canif = new CanInterface(socket, "can0");
            socket.bind(canif);
            //socket.setLoopbackMode(true);
            CanId ci = new CanId(18874401);
            ci.setEFFSFF();
            log.debug("CanId: " + ci.getCanId_EFF());
            socket.send(new CanFrame(canif,
                    ci, new byte[] {0,0,0,0,0,0,0,0}));
      }
      catch (Exception e)
      {
    	  log.error("Oops");
      }
	}
	private static void initLog4J()
	{
		InputStream is = VolvoCANBUS.class.getResourceAsStream("/log4j.properties");
		PropertyConfigurator.configure(is);
	}

}
