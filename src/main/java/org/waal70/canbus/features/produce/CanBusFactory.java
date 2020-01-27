/**
 * 
 */
package org.waal70.canbus.features.produce;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.waal70.canbus.application.VolvoCANBUS;
import org.waal70.canbus.features.queue.CanBus;
import org.waal70.canbus.features.queue.CanMessageQueue;
import org.waal70.canbus.util.OSCapability;
import org.waal70.canbus.util.OSCapability.OS;

/**
 * @author awaal Factory class to hide the underlying CanBUS implementation If
 *         on Mac OSX; it defaults to the file-based CanBus 
 *         On any other system,
 *         it will take the CanBus as specified in the properties file
 * 
 */
class CanBusFactory {

	/**
	 * 
	 */

	private static Logger log = LogManager.getLogger(CanBusFactory.class);

	CanBusFactory() {
		
	}

	static CanBus getCanBus() {
		if (OSCapability.determineOS() == OS.MAC) {
			// Operating system is Apple OSX based
			VolvoCANBUS.prop.put("VolvoCANBUS.CanBusType", "FILEBASED");
			VolvoCANBUS.prop.put("VolvoCANBUS.SendProcess", VolvoCANBUS.prop.getProperty("VolvoCANBUS.SendProcessMAC"));
			log.info("The CanBusFactory is going to override your choice for CanBusType.");
		}
		log.info("CanBusFactory is creating a reader based on the set type: "
				+ VolvoCANBUS.prop.getProperty("VolvoCANBUS.CanBusType"));

		if (VolvoCANBUS.prop.getProperty("VolvoCANBUS.CanBusType").equalsIgnoreCase("FILEBASED"))
			return new S60FileBasedCanBus(CanMessageQueue.getInstance());
		else
			return new S60IFBasedCanBus(CanMessageQueue.getInstance());
	}

}
