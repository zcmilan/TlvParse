package com.gmrz.utils.tlv.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;

public class UtilByte {

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
		Util.setShort16(tmp, (short)0, value);
		result = Util.getShort(tmp, (short)0);
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

	public static byte[] base642byte(String base64){
		return Base64.decodeBase64(base64);
	}

	public static String byte2base64(byte[] raw) {
		return Base64.encodeBase64URLSafeString(raw);
	}

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
}
