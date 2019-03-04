/**
 * 
 */
package org.waal70.canbus.util.net;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * @author awaal
 *
 */
public class ProbeInterface {
	private static Logger log = Logger.getLogger(ProbeInterface.class);

	// from http://lxr.linux.no/linux+v3.0/include/linux/if_arp.h#L30:
	private static final int ARPHRD_CAN = 280;
//	private static final int ARPHRD_LOOPBACK = 772;
//	private static final int ARPHRD_ETHER = 1;

	private static final int IFF_UP = 0x1; /* interface is up */
//	private static final int IFF_BROADCAST = 0x2; /* broadcast address valid */
//	private static final int IFF_DEBUG = 0x4; /* turn on debugging */
//	private static final int IFF_LOOPBACK = 0x8; /* is a loopback net */
//	private static final int IFF_POINTOPOINT = 0x10; /* interface is has p-p link */
//	private static final int IFF_NOTRAILERS = 0x20; /* avoid use of trailers */
//	private static final int IFF_RUNNING = 0x40;/* interface RFC2863 OPER_UP */
//	private static final int IFF_NOARP = 0x80; /* no ARP protocol */
//	private static final int IFF_PROMISC = 0x100; /* receive all packets */
//	private static final int IFF_ALLMULTI = 0x200; /* receive all multicast packets */

//	private static final int IFF_MASTER = 0x400; /* master of a load balancer */
//	private static final int IFF_SLAVE = 0x800; /* slave of a load balancer */

//	private static final int IFF_MULTICAST = 0x1000; /* Supports multicast */

//	private static final int IFF_PORTSEL = 0x2000; /* can set media type */
//	private static final int IFF_AUTOMEDIA = 0x4000; /* auto media select active */
//	private static final int IFF_DYNAMIC = 0x8000; /* dialup device with changing addresses */

//	private static final int IFF_LOWER_UP = 0x10000; /* driver signals L1 up */
//	private static final int IFF_DORMANT = 0x20000; /* driver signals dormant */

//	private static final int IFF_ECHO = 0x40000; /* echo sent packets */

	/**
	 * 
	 */
	public ProbeInterface() {
	}

	public static boolean findInterface(String searchString) {
		NetworkInterface ni = null;
		try {
			ni = NetworkInterface.getByName(searchString);
		} catch (SocketException e) {
			log.error("SocketException when searching for " + searchString + ", " + e.getLocalizedMessage());
			return false;
		}
		if (ni == null) {
			log.debug("Specified interface not found. Searched for " + searchString);
			return false;
		}

		else {
			log.debug("findInterface interface found! " + ni.getName());
			return true;
		}
	}

	public static void listInterfaces() {
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
		log.debug("Name: " + netint.getName());
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			log.debug("InetAddress: " + inetAddress);
		}
		log.debug("\n");
	}


	public static boolean confirmCanBusPresentAndActive() {
		if (System.getProperty("os.name").equals("Linux")) {

			// Read all available device names
			List<String> devices = new ArrayList<>();
			Pattern pattern = Pattern.compile("^ *(.*):");
			try (FileReader reader = new FileReader("/proc/net/dev")) {
				BufferedReader in = new BufferedReader(reader);
				String line = null;
				while ((line = in.readLine()) != null) {
					Matcher m = pattern.matcher(line);
					if (m.find()) {
						devices.add(m.group(1));
						//log.debug(m.group(1).toString());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

			// read the type and flags for each device:
			for (String device : devices) {
				try (FileReader typeReader = new FileReader("/sys/class/net/" + device + "/type")) {
					
					
					BufferedReader br_type = new BufferedReader(typeReader);
					int type = Integer.decode(br_type.readLine());
					// If we have determined the type to be CanBus, then check the flags.
					if ((type & ARPHRD_CAN) == ARPHRD_CAN)
					{
						log.debug("Presence of CANBUS confirmed.");
						br_type.close();
						typeReader.close();
						FileReader flagsReader = new FileReader("/sys/class/net/" + device + "/flags");
						BufferedReader br_flags = new BufferedReader(flagsReader);
						int flags = Integer.decode(br_flags.readLine());
						br_flags.close();
						flagsReader.close();
						if ((flags & IFF_UP) == IFF_UP) 
							{
								log.debug("CANBUS is determined to be UP.");
								return true;
							}
						else
						{
							log.debug("CANBUS is determined to be DOWN.");
							return false;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

		} else {
			// use standard API for Windows & Others (need to test on each platform,
			// though!!)
			return false;
		}
		return false;
	}

}
