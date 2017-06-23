package com.gmrz.utils.tlv.util;


import com.gmrz.utils.tlv.constant.ConstantTag;
import com.gmrz.utils.tlv.tlv.AkBeanTlv;

import java.util.List;

public class UtilTlv {

	private static final String TAG = "UtilTlv";

	//read little endian bytes
	public static AkBeanTlv readTLV(byte[] bytes, short offset){
		Logger.d(TAG,"readTLV hex:"+UtilByte.byte2hex(bytes));
		AkBeanTlv result = new AkBeanTlv();
		short lEndianTag = Util.getShort(bytes,offset);
		short tag = UtilByte.revert(lEndianTag);
		result.setTag(tag);
		short lEndianLength = Util.getShort(bytes, offset+2);
		short length = UtilByte.revert(lEndianLength);
		byte[] tmp = new byte[length];
		Util.arrayCopy(bytes,(short)(offset + AkBeanTlv.TLV_LENGTH_SIZE + AkBeanTlv.TLV_TAG_SIZE),tmp,(short)0,length);
		result.setValue(tmp);
		if(verifyEmpty(result.getValue())){

		}else if(verifySingle(result.getValue())){
			AkBeanTlv tlv = readTLV(result.getValue(),(short)0);
			AkBeanTlv[] tmps = new AkBeanTlv[1];
			tmps[0] = tlv;
			result.setValue(tmps);
		}else if(verifyArrays(result.getValue()) > 0){
			short tagsCounter = verifyArrays(result.getValue());
			short position = 0;
			short i = 0;
			AkBeanTlv[] tmps = new AkBeanTlv[tagsCounter]; 
			while(position < (result.getLength() - 1)){
				AkBeanTlv tlv = readTLV(result.getValue(), position);
				tmps[i] = tlv;
				i++;
				position += AkBeanTlv.TLV_LENGTH_SIZE + AkBeanTlv.TLV_TAG_SIZE + tlv.getLength();
			}
			result.setValue(tmps);
		}
		return result;
	}
	
	//write little endian bytes
	public static byte[] writeTLV(AkBeanTlv tlv){
		if(tlv == null){
			return null;
		}
		if(tlv.getLength() == 0 || tlv.getValue() == null){
			//data is empty
			Logger.d(TAG,"tlv length is 0");
			short size = (short) (AkBeanTlv.TLV_TAG_SIZE + AkBeanTlv.TLV_LENGTH_SIZE);
			byte[] result = new byte[size];
			short lEndianTag = UtilByte.revert(tlv.getTag());
			Util.setShort16(result, (short) 0, lEndianTag);
			short lEndianLength = UtilByte.revert(tlv.getLength());
			Util.setShort16(result, (short) 2, lEndianLength);
			return result;
		}else{
			if(tlv.getValue().length < tlv.getLength()){
				return null;
			}
			short size = (short) (AkBeanTlv.TLV_TAG_SIZE + AkBeanTlv.TLV_LENGTH_SIZE + tlv.getLength());
			byte[] result = new byte[size];
			short lEndianTag = UtilByte.revert(tlv.getTag());
			Util.setShort16(result, (short) 0, lEndianTag);
			short lEndianLength = UtilByte.revert(tlv.getLength());
			Util.setShort16(result, (short) 2, lEndianLength);
			if(tlv.getLength() == 0){
				Logger.d(TAG,"tlv length is 0");
			}else{
				Util.arrayCopy(tlv.getValue(), (short)0, result, (short)4, tlv.getLength());
			}
			return result;
		}
	}
	
	//write little endian bytes
	public static byte[] writeTLV(AkBeanTlv[] tlvs){
		if(tlvs == null || tlvs.length <= 0){
			return null;
		}
		byte[] value;
		short length = 0;
		short position = 0;
		for(short i = 0 ; i < tlvs.length ; i++){
			AkBeanTlv tlv = tlvs[i];
			if(tlv != null){
				byte[] tmp = tlv.toBytes();
				length+=tmp.length;
			}
		}
		value = new byte[length];
		for(short i = 0 ; i < tlvs.length ; i++){
			AkBeanTlv tlv = tlvs[i];
			if(tlv != null){
				byte[] tmp = tlv.toBytes();
				Util.arrayCopy(tmp, (short)0, value, position, (short)tmp.length);
				position+=tmp.length;
			}
		}
		return value;
	}

	//write little endian bytes
	public static byte[] writeTLV(List<AkBeanTlv> tlvs){
		if(tlvs == null || tlvs.size() <= 0){
			return null;
		}
		byte[] value;
		short length = 0;
		short position = 0;
		for(short i = 0 ; i < tlvs.size() ; i++){
			AkBeanTlv tlv = tlvs.get(i);
			if(tlv != null){
				byte[] tmp = tlv.toBytes();
				length+=tmp.length;
			}
		}
		value = new byte[length];
		for(short i = 0 ; i < tlvs.size() ; i++){
			AkBeanTlv tlv = tlvs.get(i);
			if(tlv != null){
				byte[] tmp = tlv.toBytes();
				Util.arrayCopy(tmp, (short)0, value, position, (short)tmp.length);
				position+=tmp.length;
			}
		}
		return value;
	}

	public static boolean verifyEmpty(byte[] bytes){
		if(bytes == null || bytes.length <= 0){
			return true;
		}
		return false;
	}

	public static boolean verifySingle(byte[] bytes){
		if(bytes.length < (AkBeanTlv.TLV_LENGTH_SIZE + AkBeanTlv.TLV_TAG_SIZE) ){
			return false;
		}
		
		short lEndianTag = Util.getShort(bytes,0);
		short tag = UtilByte.revert(lEndianTag);
		if(!ConstantTag.TAGMAP.containsKey(tag)){
			return false;
		}
		short size = (short)bytes.length;
		short lEndianlength = Util.getShort(bytes, (short)2);
		short length = UtilByte.revert(lEndianlength);
		if(length != size - (AkBeanTlv.TLV_LENGTH_SIZE + AkBeanTlv.TLV_TAG_SIZE)){
			return false;
		}
		return true;
	}
	
	public static short verifyArrays(byte[] bytes){
		if(bytes.length < (AkBeanTlv.TLV_LENGTH_SIZE + AkBeanTlv.TLV_TAG_SIZE) ){
			return 0;
		}
		short counter = 0;
		short size = (short)bytes.length;
		short position = 0;
		while(position < size){
			short lEndianTag = Util.getShort(bytes,0);
			short tag = UtilByte.revert(lEndianTag);
			if(!ConstantTag.TAGMAP.containsKey(tag)){
				return 0;
			}
			short lEndianLength = Util.getShort(bytes, (short)(2+position));
			short length = UtilByte.revert(lEndianLength);
			short endPosition = (short) (position + AkBeanTlv.TLV_LENGTH_SIZE + AkBeanTlv.TLV_TAG_SIZE);
			if(length > size - endPosition){
				return 0;
			}
			counter++;
			position = (short) (endPosition + length);
		}
		return counter;
	}
}
