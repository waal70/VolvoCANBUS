/**
 * 
 */
package org.waal70.canbus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.waal70.canbus.CanMessage;
/**
 * @author awaal
 * @param <CanMessage>
 *
 */

public class CanMessageQueue implements Queue<CanMessage> {
	private static Logger log = Logger.getLogger(CanMessageQueue.class);
	private List<CanMessage> _Canmsg = new ArrayList<>();
	private int MAX_LENGTH = Integer.MAX_VALUE;

	public int size() {
		return _Canmsg.size();
	}
	public void setQueueLength(int newValue) {
		if (newValue < Integer.MAX_VALUE)
			MAX_LENGTH = newValue;
		else
		{
			log.error("Trying to set queue length higher than max allowed. Setting to max allowed.");
			MAX_LENGTH = Integer.MAX_VALUE;
		}
	}

	public boolean isEmpty() {
		return _Canmsg.isEmpty();
	}

	public boolean contains(Object o) {
		return _Canmsg.contains(o);
	}

	public Iterator<CanMessage> iterator() {
		return _Canmsg.iterator();

	}

	public Object[] toArray() {
		return _Canmsg.toArray();
		
	}

	public <T> T[] toArray(T[] a) {
		return _Canmsg.toArray(a);
	}

	public boolean remove(Object o) {
		return _Canmsg.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return _Canmsg.containsAll(c);
	}

	public boolean addAll(Collection<? extends CanMessage> c) {
		if ((c.size() + _Canmsg.size()) > MAX_LENGTH)
		{
			log.error("Adding this collection would result in an array that is too big.");
			return false;
		}
		return _Canmsg.addAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		return _Canmsg.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return _Canmsg.retainAll(c);
	}

	public void clear() {
		_Canmsg.clear();
	}

	public boolean add(CanMessage e) {
		log.info("Adding message to queue: [" + e.messageAsCommand() + "]");
		return _Canmsg.add(e);
		
	}

	/**
	 * dequeues a message, meaning return the first one on the queue
	 * and subsequently remove it from the queue, moving the next in
	 * line up to the front of the queue
	 * @return
	 * CanMessage
	 */
	public CanMessage dequeue() {
		if (_Canmsg.size() == 0) throw new java.util.NoSuchElementException();
		CanMessage returnthis = _Canmsg.get(0); //gets the first element
		_Canmsg.remove(0);
		return returnthis;
		
	}

	public CanMessage remove() {
		//This method removes head of queue
		return _Canmsg.remove(0);
	}

	public CanMessage poll() {
		//This message returns null when queue is empty
		if (_Canmsg.size() == 0) return null;
		return _Canmsg.remove(0);
	}

	public CanMessage element() {
		if (_Canmsg.size() == 0) throw new java.util.NoSuchElementException();
		return _Canmsg.get(0);
	}

	public CanMessage peek() {
		if (_Canmsg.size() == 0) return null;
		//_Canmsg.remove(0); peeking means not removing
		return  _Canmsg.get(0); //gets the first element;
	}

	@Override
	public boolean offer(CanMessage e) {
		//TODO add capacity checking
		return this.add(e);
	}


}
