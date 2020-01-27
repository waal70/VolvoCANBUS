/**
 * 
 */
package org.waal70.canbus.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author awaal
 *
 */
public class OSCapability {

	/**
	 * 
	 */
	public enum OS {
		MAC("M"),
		WINDOWS("W"),
		LINUX("L"),
		OTHER("O");
		
		String key;

	     OS(String key) { this.key = key; }

	     //default constructor, used only for the OTHER case, 
	     //because OTHER doesn't need a key to be associated with. 
	     OS() { }

	     OS getValue(String x) {
	         if ("M".equals(x)) { return MAC; }
	         else if ("W".equals(x)) { return WINDOWS; }
	         else if ("L".equals(x)) { return LINUX; }
	         else if (x == null) { return OTHER; }
	         else throw new IllegalArgumentException();
	     }
	}
	
	private static Logger log = LogManager.getLogger(OSCapability.class);
	
	public static OS determineOS()
	{
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("mac")) {
			// Operating system is Apple OSX based
			log.info("Detected Apple OSX.");
			return OS.MAC;
		}
		else if (os.contains("win")) {
			log.info("Detected Windows.");
			return OS.WINDOWS;
		}
		else if (os.contains("linux")) {
			log.info("Detected Linux.");
			return OS.LINUX;
		}
		else {
			log.info("Unknown OS: " + os);
			return OS.OTHER;
		}
	}
	public static boolean isGUI()
	{
		//For now, only show GUI on MAC
		if (determineOS()==OS.MAC)
			return true;
		else
			return false;
	}

}
