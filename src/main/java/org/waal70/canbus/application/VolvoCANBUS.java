/**
 * 
 */
package org.waal70.canbus.application;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.waal70.canbus.CanSocket;
import org.waal70.canbus.CanSocket.CanFrame;
import org.waal70.canbus.CanSocket.CanId;
import org.waal70.canbus.CanSocket.CanInterface;
import org.waal70.canbus.CanSocket.Mode;
import org.waal70.canbus.application.process.VolvoCANBUSProcess;
import org.waal70.canbus.application.ui.VolvoCANBUSFXML;
import org.waal70.canbus.util.OSCapability;
import org.waal70.canbus.util.ProbeInterface;

/**
 * @author awaal
 * This is the main class
 * Class is also the broker, managing the creation of a queue,
 * The producers, and the consumers
 */
public class VolvoCANBUS {
	private static Logger log = LogManager.getLogger(VolvoCANBUS.class);
	public static Properties prop = new Properties();
	

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//initLog4J();
		initProperties();
		log.info("Program start.");
		if (OSCapability.isGUI())
			VolvoCANBUSFXML.main(args);
		else
			VolvoCANBUSProcess.doCanbusAlt();
		
		
		//This will test the canbus:
		//String one = args[0];
		//doCanbusAlt();
		//this will test the utilities
		//testUtil();

		log.info("Program end.");
		//System.exit(0);


	}
	
	@SuppressWarnings("unused")
	private static void testUtil() throws SocketException
	{
		ProbeInterface.listInterfaces();
		log.debug(ProbeInterface.confirmCanBusPresentAndActive());
	}
	
	

/*	private static void doCanbus() throws Exception
	{
		
		
		S60CanBusReader scbr = new S60CanBusReader("Single Reader");
		scbr.start();
		if (VolvoCANBUS.prop.getProperty("VolvoCANBUS.CanBusType").equalsIgnoreCase("IFBASED"))
			canBusWrite();
		
		while (scbr.isAlive())
		{
		   TimeUnit.MILLISECONDS.sleep(1000);
		   log.debug("waiting for thread to die");
		}
		log.debug("Thread no longer alive..");
		 scbr.join(5000);
		 
		log.info("Number of messages in queue: " + CanMessageQueue.getInstance().size());
		
	}*/
	


	@SuppressWarnings("unused")
	private static void canBusWrite()
	{
		try (final CanSocket socket = new CanSocket(Mode.RAW)) {
            final CanInterface canif = new CanInterface(socket, "can0");
            socket.bind(canif);
            CanId ci = new CanId(0x123456);
            ci.setEFFSFF();
            log.debug("CanId: " + ci.getCanId_EFFHex());
            socket.send(new CanFrame(canif,
                    ci, new byte[] {0,0,0,0,0,0,0,0}));
      }
      catch (Exception e)
      {
    	  log.error("Oops");
      }
	}

	private static void initProperties()
	{
		InputStream is = VolvoCANBUS.class.getResourceAsStream("/VolvoCANBUS.properties");
		try {
			prop.load(is);
		} catch (IOException e) {
			log.error("Cannot find VolvoCANBUS.properties. Using defaults");
			prop.put("VolvoCANBUS.CanBusType", "IFBASED");
			prop.put("VolvoCANBUS.SendProcess", "/home/awaal/cansend");
		}
	}

}
