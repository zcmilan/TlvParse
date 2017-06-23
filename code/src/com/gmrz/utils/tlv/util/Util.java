package com.gmrz.utils.tlv.util;

import java.util.concurrent.ThreadLocalRandom;

public class Util {

	private static final String TAG = Util.class.getSimpleName();
	
	public static final int arrayCopy (byte[] src, int srcOff, byte[] dest, int destOff, int length){
		int result = -1;
		if(src.length < srcOff){
			Logger.e(TAG, "arrayCopy return error, src.length < srcOff");
			return result;
		}
		if(src.length < (length + srcOff)){
			Logger.e(TAG, "arrayCopy return error, src.length < srcOff");
			return result;
		}
		if(dest.length < destOff){
			return result;
		}
		if(dest.length < (destOff+length)){
			return result;
		}
		int srcPosition=srcOff;
		int destPosition=destOff;
		for(int i=0;i<length;i++){
			dest[destPosition]=src[srcPosition];
			destPosition++;
			srcPosition++;
		}
		return destPosition;
	}
	
	public static final short getShort (byte[] bArray, int bOff) {
		return (short) (((bArray[bOff + 1] << 8) | bArray[bOff + 0] & 0xff));  
	}
	
	public static final short setShort16(byte[] bArray, short bOff, short sValue){
		bArray[bOff + 1] = (byte) (sValue >> 8);  
		bArray[bOff + 0] = (byte) (sValue >> 0);  
		return (short)(bOff + 2);
	}

	public static final short setShort8(byte[] bArray, short bOff, short sValue){
		bArray[bOff + 0] = (byte) (sValue >> 0);
		return (short)(bOff + 1);
	}
	
	public static final short makeShort(byte one , byte two){
		return (short) (((one << 8) | two & 0xff));  
	}
	
	public static String random(int length) {  
	    StringBuilder builder = new StringBuilder(length);  
	    for (int i = 0; i < length; i++) {  
	        builder.append((char) (ThreadLocalRandom.current().nextInt(33, 128)));  
	    }  
	    return builder.toString();  
	} 
}
