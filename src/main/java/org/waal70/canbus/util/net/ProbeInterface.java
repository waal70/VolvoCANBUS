/**
 * 
 */
package org.waal70.canbus.util.net;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import org.apache.log4j.Logger;
/**
 * @author awaal
 *
 */
public class ProbeInterface {
	private static Logger log = Logger.getLogger(ProbeInterface.class);

	/**
	 * 
	 */
	public ProbeInterface() {
	}
	
	public static boolean findInterface(String searchString)
	{
		NetworkInterface ni = null;
		try {
			ni = NetworkInterface.getByName(searchString);
		} catch (SocketException e) {
			log.error("SocketException when searching for " + searchString + ", " + e.getLocalizedMessage());
			return false;
		}
		if (ni == null)
		{
			log.debug("Specified interface not found. Searched for " + searchString);
			return false;
		}
			
		else
		{
			log.debug("findInterface interface found! " + ni.getName());
			return true;
		}
	}
	
	public void listInterfaces() {
        Enumeration<NetworkInterface> nets = null;
		try {
			nets = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        for (NetworkInterface netint : Collections.list(nets))
			try {
				displayInterfaceInformation(netint);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        log.debug("Display name: " + netint.getDisplayName());
        log.debug("Name: " +  netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            log.debug("InetAddress: "+ inetAddress);
        }
        log.debug("\n");
     }
}  
