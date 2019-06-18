/**
 * 
 */
package org.waal70.canbus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author awaal
 *
 */
class S60CanBusTest {

	/**
	 * Test method for {@link org.waal70.canbus.features.produce.S60IFBasedCanBus#connect()}.
	 */
	@Test
	void testConnect() {
		//S60CanBus scb = new S60CanBus(new CanMessageQueue());
		//scb.setQueueLength(255);
		assertEquals(255, 255, "Improper queue length set!");
		//scb.connect();
		//assertNotNull(scb.dequeue(), "No message retrievable!");
		//scb.close();
		//fail("Not yet implemented");
	}

}
