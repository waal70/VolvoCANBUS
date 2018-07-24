/**
 * 
 */
package org.waal70.canbus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author awaal
 *
 */
class S60CanBusTest {

	/**
	 * Test method for {@link org.waal70.canbus.S60CanBus#connect()}.
	 */
	@org.junit.jupiter.api.Test
	void testConnect() {
		S60CanBus scb = new S60CanBus(new CanMessageQueue());
		scb.setQueueLength(255);
		assertEquals(255, scb.getQueueLength(), "Improper queue length set!");
		scb.connect();
		assertNotNull(scb.dequeue(), "No message retrievable!");
		scb.close();
		//fail("Not yet implemented");
	}

}
