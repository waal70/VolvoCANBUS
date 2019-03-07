/**
 * 
 */
package org.waal70.canbus;

import org.apache.log4j.Logger;
import org.waal70.canbus.CanSocket.CanFrame;
import org.waal70.canbus.CanSocket.CanId;

/**
 * @author awaal
 *
 */
public class CanMessage extends CanBusMessage {
	private static Logger log = Logger.getLogger(CanMessage.class);
	private static final int MSG_LENGTH = 16;
	@SuppressWarnings("unused")
	private String _interfaceName;
	@SuppressWarnings("unused")
	private int _numBytes = 0;
	private CanId _canId = null;
	private String _canData = ""; //HEX string, always 8 bytes long, or 16 characters
	//send format is ./cansend can0 12312312#ABCDEF4455667788
	
	private static String pruneHexString(String s) {
		//this method ensures the member variables in this class
		// will only have valid lengths (16 characters for CanData)
		
		//log.debug("pruneHexString input received: " + s);
		s = s.replaceAll("\\s","");
		//log.debug("After strip spaces: " + s);
		int len = s.length();
		if (len > MSG_LENGTH)
		{
			s = s.substring(0,MSG_LENGTH);
			log.info("Input too long, I cropped your input to 8 bytes: (" + s +")");
		}
		if (len < MSG_LENGTH)
		{
			log.info("Input too short, prepending zeroes.");
			StringBuilder sb = new StringBuilder();

			for (int toPrepend=MSG_LENGTH-s.length(); toPrepend>0; toPrepend--) {
			    sb.append('0');
			}

			sb.append(s);
			s = sb.toString();
		}
		return s;
		
		
	}

	public static byte[] hexStringToByteArray(String s) {
		s = pruneHexString(s);
		int len = s.length();
		byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	/**
	 * @return the _canId
	 */
	public CanId getCanId() {
		return _canId;
	}
	/**
	 * @param _canId the _canId to set
	 */
	public void setCanId(CanId _canId) {
		this._canId = _canId;
	}
	/**
	 * @return the _canData
	 */
	public String getCanMessage() {
		return _canData;
	}
	/**
	 * @param _canData the _canData to set
	 * Please adhere to providing your input as a HEX string
	 * DO NOT specify 0x, but rather a string of HEX pairs, such
	 * as 00 AA BB CC DD EE FF 11
	 * I have limited the message to 8 bytes (8x2 characters).
	 * Please observe the cropping/padding that will take place
	 * if you provide an incorrect number of bytes.
	 */
	public void setCanMessage(String _canData) {
		_canData = pruneHexString(_canData);
		this._canData = _canData;
	}
	
	public CanMessage()
	{
		super();
	}
	public CanMessage(String strCanId, String _canData) {
		super();
		this.setCanMessage(strCanId, _canData);
	}
	
	public void setCanMessage(String strCanId, String _canData) {
		_canId = new CanId(Integer.parseInt(strCanId));
		if (strCanId.length() <= 3) 
			_canId.clearEFFSFF();
				
		_canData = pruneHexString(_canData);
		this._canData = _canData;
	}

	@Override
	public String getHexCanId() {
		return _canId.getCanId_EFFHex();
	}

	@Override
	public String getMessage() {
		return this._canData;
	}
	
	public boolean parseReal(CanFrame _cf)
	{
		byte[] data = _cf.getData();
		log.info("isSetEFFSFF yields: " +_cf.getCanId().isSetEFFSFF());
		log.info("CanId SFF: "+ _cf.getCanId().getCanId_SFF());
		log.info("CanId EFF: "+ _cf.getCanId().getCanId_EFF());
		this._canData = bytesToHex(data);
		log.info("HEX representation of data is: " + this._canData);
		return true;
	}
	
	public boolean parseMessage(String wholeMessage)
	{
		//return true;
		//So I have received a String, that looks like
		// can0  12312312   [8]  11 22 33 44 55 66 77 88
		 String[] wholeMessageSplit = wholeMessage.split("\\s");
		 //should yield AT LEAST 11 parts...
		 if (wholeMessageSplit.length < 11)
		 {
			 log.error("Too few parts received. Discarding: [" + wholeMessage + "]");
			 return false;
		 }
		 String[] result = new String[11];
		 int resultIndex = 0;
		 for (int i=0; i< wholeMessageSplit.length;i++)
		 {
			 if (wholeMessageSplit[i].trim().length() != 0)
				{
				 result[resultIndex] = wholeMessageSplit[i];
				 resultIndex++;
				}
				 
		 }
		 return parseSplitMessage(result);
		 //return true;
	}
	private boolean parseSplitMessage(String[] result)
	{
		//First element is interfacename
		this._interfaceName = result[0];
		//log.debug("interfaceName = " + _interfaceName);
		//Second element is canid in String format
		
		this._canId = new CanId(Integer.parseInt(result[1],16));
		//log.debug("canID: " + _canId.getCanId_EFFHex());
		//Third element is datalength
		this._numBytes = Integer.parseInt(result[2].substring(1,2));
		//Then, 8 bytes of data:
		String msg = "";
		for (int i = 0; i < 8; i++)
			msg += result[3+i];
		this._canData = msg;
		return true;
		
	}

	@Override
	public String messageAsCommand() {
		final String separator = "#";
		String cmdText = "";
		if (this._canId!= null)
		{
			cmdText = (_canId.isSetEFFSFF()) ? _canId.getCanId_EFFHex() : _canId.getCanId_SFFHex();
		}
		cmdText += separator;
		cmdText += _canData;
		
		return cmdText;
	}

}
