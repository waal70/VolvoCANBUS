package org.waal70.canbus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.waal70.canbus.application.VolvoCANBUS;

class initTest {
	public static Properties prop = new Properties();
	private static Logger log = Logger.getLogger(initTest.class);

	@BeforeAll
	static void testInit() {
		initProperties();
		initLog4J();
		//fail("hahah");
	}
	
	@Test
	void runme()
	{
		testInit();
	}
	private static void initProperties()
	{
		InputStream is = VolvoCANBUS.class.getResourceAsStream("/VolvoCANBUS.properties");
		try {
			prop.load(is);
			prop.put("VolvoCANBUS.CanBusType", "FILEBASED");
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
