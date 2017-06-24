package com.gmrz.utils.tlv.tlv;


import com.gmrz.utils.tlv.util.UtilByte;
import com.gmrz.utils.tlv.util.UtilTlv;

import java.nio.ByteBuffer;

public class AkBeanTlv {
	
	public static final short TLV_TAG_SIZE = 2;
	public static final short TLV_LENGTH_SIZE = 2;
	
	private short tag;//save as normal,not little endian
	
	private short length = 0;
	
	private byte[] value;
	
	private AkBeanTlv[] subTlv;
	
	public AkBeanTlv(){
		
	}
	
	public AkBeanTlv(short tag){
		this.tag = tag;
	}
	
	public static AkBeanTlv Build(short tag){
		return new AkBeanTlv(tag);
	}
	
	public AkBeanTlv setTag(short tag) {
		this.tag = tag;
		return this;
	}

	public AkBeanTlv setValue(byte[] bytes) {
		if(bytes == null || bytes.length == 0){
			this.value = null;
			this.length = 0;
		}else{
			this.value = bytes;
			this.length = (short)value.length;
		}
		return this;
	}

	
	/**
	 * setValue
	 * @param value : if >= 0 , set value ; if < 0 , set value empty
	 * @author zhangchao@gmrz-bj.com
	 *
	*/
	public AkBeanTlv setValue(short shortValue){
		if(shortValue < 0){
			this.value = null;
			this.length = 0;
		}else{
			this.value = new byte[2];
			UtilByte.setShort16(this.value, (short)0, shortValue);
			this.length = AkBeanTlv.TLV_LENGTH_SIZE;
		}
		return this;
	}

	/**
	 * setValue
	 * @param AkBeanTlv[] : if size > 0 , set value ; if size <= 0 and null , set value empty
	 * @author zhangchao@gmrz-bj.com
	 *
	*/
	public AkBeanTlv setValue(AkBeanTlv[] subTlv) {
		if(subTlv == null || subTlv.length == 0){
			this.value = null;
			this.length = 0;
		}else{
			this.subTlv = subTlv;
			int length = 0;
			for (int i = 0 ; i < subTlv.length ; i++){
				AkBeanTlv tmp = subTlv[i];
				if(tmp != null)
					length += tmp.getLength() + AkBeanTlv.TLV_LENGTH_SIZE + AkBeanTlv.TLV_TAG_SIZE;
			}
			ByteBuffer bytes = ByteBuffer.allocate(length);
			for (int i = 0 ; i < subTlv.length ; i++){
				AkBeanTlv tmp = subTlv[i];
				if(tmp != null)
					bytes.put(tmp.toBytes());
			}
			this.value = bytes.array();
			this.length = (short)this.value.length;
		}
		return this;
	}

	public short getTag() {
		short tmp = tag;
		return tmp;
	}

	public short getLength() {
		short tmp = length;
		return tmp;
	}

	public byte[] getValue() {
		return value;
	}

	public byte[] toBytes(){
		return UtilTlv.writeTLV(this);
	}

	public AkBeanTlv[] getSubTlv(){
		return subTlv;
	}

	@Override
	public String toString() {
		return UtilByte.byte2hex(toBytes());
	}


	public static AkBeanTlv BuildEmpty(short tag){
		AkBeanTlv u = new AkBeanTlv(tag);
		return u;
	}

	public static AkBeanTlv BuildBytes(short tag,byte[] bytes){
		AkBeanTlv u = new AkBeanTlv(tag);
		u.setValue(bytes);
		return u;
	}

	public static AkBeanTlv BuildShort(short tag, short tmp){
		AkBeanTlv u = new AkBeanTlv(tag);
		u.setValue(tmp);
		return u;
	}
}
