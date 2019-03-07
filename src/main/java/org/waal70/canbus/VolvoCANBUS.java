/**
 * 
 */
package org.waal70.canbus;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Properties;
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
	public static Properties prop = new Properties();
	

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		initLog4J();
		initProperties();
		log.info("Program start.");
		//This will test the canbus:
		doCanbus();
		//this will test the utilities
		//testUtil();

		log.info("Program end.");
		System.exit(0);


	}
	
	@SuppressWarnings("unused")
	private static void testUtil() throws SocketException
	{
		ProbeInterface.listInterfaces();
		log.debug(ProbeInterface.confirmCanBusPresentAndActive());
	}
	
	
	@SuppressWarnings("unused")
	private static void doCanbus() throws Exception
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
		
	}

	private static void canBusWrite()
	{
		try (final CanSocket socket = new CanSocket(Mode.RAW)) {
            final CanInterface canif = new CanInterface(socket, "can0");
            socket.bind(canif);
            //socket.setLoopbackMode(true);
            CanId ci = new CanId(123456);
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
	
	private static void initLog4J()
	{
		InputStream is = VolvoCANBUS.class.getResourceAsStream("/log4j.properties");
		PropertyConfigurator.configure(is);
	}

}
