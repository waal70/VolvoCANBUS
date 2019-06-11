/**
 * 
 */
package org.waal70.canbus.features.queue;

import org.apache.log4j.Logger;
import org.waal70.canbus.application.VolvoCANBUS;
import org.waal70.canbus.features.produce.S60FileBasedCanBus;
import org.waal70.canbus.features.produce.S60IFBasedCanBus;

/**
 * @author awaal Factory class to hide the underlying CanBUS implementation If
 *         on Mac OSX; it defaults to the file-based CanBus On any other system,
 *         it will take the CanBus as specified in the properties file
 * 
 */
public class CanBusFactory {

	/**
	 * 
	 */

	private static Logger log = Logger.getLogger(CanBusFactory.class);

	public CanBusFactory() {
		
	}

	public static CanBus getCanBus() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("mac")) {
			// Operating system is Apple OSX based
			VolvoCANBUS.prop.put("VolvoCANBUS.CanBusType", "FILEBASED");
			VolvoCANBUS.prop.put("VolvoCANBUS.SendProcess", VolvoCANBUS.prop.getProperty("VolvoCANBUS.SendProcessMAC"));
			log.info("Detected Apple OSX. The CanBusFactory is going to override your choice for CanBusType.");
		}
		log.info("CanBusFactory is creating a reader based on the set type: "
				+ VolvoCANBUS.prop.getProperty("VolvoCANBUS.CanBusType"));

		if (VolvoCANBUS.prop.getProperty("VolvoCANBUS.CanBusType").equalsIgnoreCase("FILEBASED"))
			return new S60FileBasedCanBus(CanMessageQueue.getInstance());
		else
			return new S60IFBasedCanBus(CanMessageQueue.getInstance());
	}

}
