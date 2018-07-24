/**
 * 
 */
package org.waal70.canbus;

import org.apache.log4j.Logger;

/**
 * @author awaal
 *
 */
public final class CanId implements Cloneable {
	private static Logger log = Logger.getLogger(CanId.class);
	private int _canId = 0;
	private boolean _isEFF = true; 
	/**
	 * @return the _isEFF
	 */
	public boolean isEFF() {
		return _isEFF;
	}

	/**
	 * @param _isEFF the _isEFF to set
	 */
	public void setEFF(boolean _isEFF) {
		this._isEFF = _isEFF;
	}

	static public final int CAN_SFF_MASK= 0x000007FF;
	static public final int CAN_EFF_MASK= 0x1FFFFFFF;
	static public final int BASE_HEX = 16;

	/**
	 * @param address
	 * The constructor will accept an CAN-ID address. Please take care when
	 * specifying, as it will accept HEX-formatted and regular int.
	 * By default it is assuming Extended Can-id. Please call setEFF(false) to
	 * explicitly use the standard can-id
	 */
	public CanId(int address) {
		//If I get passed an int, I will first need
		// to go back into hex format
		//log.info("Interpreting the following address as int: "+ address);
		String sAddress = Integer.toString(address,BASE_HEX);
		if (sAddress.length() > 8)
		{
			log.warn("Supplied CAN-ID is longer than Extended CAN id allows. I will crop!");
			sAddress = sAddress.substring(0,8);
			log.warn("Supplied was: " + address +", now: " + sAddress);
			address = Integer.parseInt(sAddress,BASE_HEX);
		}
		_canId = address;
		//log.debug("int address: " +address);
	}
	public CanId(String address) {
		// So if I get passed a string, I am going to assume it is a HEX-string
		//log.info("Interpreting the following address as HEX string: " + address);
		String sAddress = address;
		if (sAddress.length() > 8)
		{
			log.warn("Supplied CAN-ID is longer than Extended CAN id allows. I will crop!");
			sAddress = sAddress.substring(0,8);
			log.warn("Supplied was: " + address +", now: " + sAddress);
			address = sAddress;
		}
		//log.debug("parseInt: " + Integer.parseInt(address,BASE_HEX));
		_canId = Integer.parseInt(address,BASE_HEX);
		//log.debug("address: " + _canId);
	}

	public String getCanId_SFFHex() {
		//return the "small" can-id:
		//Do not touch the member variable
		int canId = _canId; //& CAN_SFF_MASK;
		//log.debug("canId " + canId);
		if (canId > 2047) 
		{
			log.warn("CanId too big for Standard CANID. Returning max (0x7FF)");
			canId = 0x7FF;
		}
		//_canId&=CAN_SFF_MASK;
		return String.format("0x%03X", canId);
		//return padToLength(Integer.toString(canId), SFF_LENGTH);
	}

	public String getCanId_EFFHex() {

		return String.format("0x%08X", _canId);

	}
	public String getCanId_SFF() {
		//return the "small" can-id:
		//Do not touch the member variable
		int canId = _canId; //& CAN_SFF_MASK;
		//log.debug("canId " + canId);
		if (canId > 2047) 
		{
			log.warn("CanId too big for Standard CANID. Returning max (0x7FF)");
			canId = 0x7FF;
		}
		//_canId&=CAN_SFF_MASK;
		return String.format("%03X", canId);
		//return padToLength(Integer.toString(canId), SFF_LENGTH);
	}

	public String getCanId_EFF() {

		return String.format("%08X", _canId);

	}


	@Override
	protected Object clone() {
		return new CanId(_canId);
	}

	@Override
	public String toString() {
		return "CanId [canId=" + (_isEFF
				? getCanId_EFF() : getCanId_SFF()) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _canId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CanId other = (CanId) obj;
		if (_canId != other._canId)
			return false;
		return true;
	}
}