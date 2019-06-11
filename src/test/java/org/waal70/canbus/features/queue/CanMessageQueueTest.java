package org.waal70.canbus.features.queue;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class)
class CanMessageQueueTest {
	
	private static CanMessageQueue _cmq = CanMessageQueue.getInstance();
	

	@Test
	void testGetInstance() {
		CanMessageQueue cmq = CanMessageQueue.getInstance();
		assertEquals(CanMessageQueue.class, cmq.getClass(), "Not able to retrieve queue instance.");
	}

	@Test
	@Order(1)
	void testAddCanMessage() {
		CanMessage cm = new CanMessage("12345", "testing");
		assertTrue(_cmq.add(cm), "Unable to add message to queue");
			}

	@Test
	void testSetInactive() {
		assertTrue(_cmq.isActive(), "Initial state not active");
		_cmq.setInactive();
		assertFalse(_cmq.isActive(), "State not altered by setInactive()");
	}

	@Test
	void testIsActive() {
		@SuppressWarnings("unused")
		boolean result;
		result = _cmq.isActive();
		
	}

	@Test
	@Order(2)
	void testGet() {
		CanMessage cm = _cmq.get();
		assertEquals("000000000testing", cm.getCanMessage());
		
	}

}
