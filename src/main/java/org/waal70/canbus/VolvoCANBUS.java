/**
 * 
 */
package org.waal70.canbus;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author awaal
 *
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
		S60CanBus scb = new S60CanBus(new CanMessageQueue());
		scb.setQueueLength(255);
		scb.connect();
		log.info(scb.dequeue().messageAsCommand());
		scb.close();
		
	 	 
		 //cm.setCanId("12312312");
		 //the extended can-id has 4 bytes (actually 29 bits, but who cares)
		 // the regular can-id has 3 characters (11 bits, but who cares)

		 // filter on can-id 12312312:
		 //./candump can0,12312312:1fffffff
		 //
		 // messages arrive as (HEX encoded):
		 //  can0  12312312   [8]  11 22 33 44 55 66 77 88


	}
	private static void initLog4J()
	{
		InputStream is = VolvoCANBUS.class.getResourceAsStream("/log4j.properties");
		PropertyConfigurator.configure(is);
	}

}
