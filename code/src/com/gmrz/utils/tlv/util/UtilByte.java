package com.gmrz.utils.tlv.util;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ThreadLocalRandom;


public class UtilByte {
	
	private static final String TAG = "UtilByte";

	public static void revert(byte[] bytes){
		short i=0;
		short length = (short)bytes.length;
		while(i < length - i - 1){
			byte tmp = bytes[i];
			bytes[i] = bytes[length-i-1];
			bytes[length-i-1] = tmp;
			i++;
		}
	}
	
	//function has problem
	public static short revert(short value){
		byte[] tmp = new byte[2];
		short result = (short)0;
		setShort16(tmp, (short)0, value);
		result = getShort(tmp, (short)0);
		return result;
	}
	
//	public static String toBase64(byte[] bytes){
//		String result = Base64.encodeBase64URLSafeString(bytes);
//		return result;
//	}
//	
//	public static byte[] fromBase64(String base64){
//		byte[] result = Base64.decodeBase64(base64);
//		return result;
//	}

	public static String byte2hex(byte[] raw) {
		if(raw == null){
			return "bytes are null";
		}
		String arHex = "0123456789ABCDEF";
		StringBuilder hex = new StringBuilder(2 * raw.length);
		for (byte b : raw) {
			hex.append("0123456789ABCDEF".charAt((b & 0xF0) >> 4)).append("0123456789ABCDEF".charAt(b & 0xF));
		}
		return hex.toString();
	}

	public static byte[] hex2byte(String hexs) {
		byte[] res = new byte[hexs.length()/2];
		char[] chs = hexs.toCharArray();
		for(int i=0,c=0; i<chs.length; i+=2,c++){
			res[c] = (byte) (Integer.parseInt(new String(chs,i,2), 16));
		}
		return res;
	}

	public static int arraycopy(byte[] src,int srcpos,byte[] dest,int destpos,int length){
		int nextPos = destpos + length;
		System.arraycopy(src,srcpos,dest,destpos,length);
		return nextPos;
	}

//	public static byte[] base642byte(String base64){
//		return Base64.decodeBase64(base64);
//	}
//
//	public static String byte2base64(byte[] raw) {
//		return Base64.encodeBase64URLSafeString(raw);
//	}

	public static boolean isSame(byte[] a1,byte[] a2){
		if(a1.length != a2.length){
			return false;
		}
		int length = a1.length;
		for (int i=0;i<length;i++){
			if(a1[i] == a2[i]){
				continue;
			}else {
				return false;
			}
		}
		return true;

	}

	public static byte[] str2byte(String value){
		byte[] bytes = null;
		if(value != null){
			try {
				bytes = value.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			bytes = new byte[0];
		}
		return bytes;
	}

	public static String byte2str(byte[] bytes){
		String str = "";
		if(bytes != null){
			try {
				str = new String(bytes,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str;
	}
	
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
