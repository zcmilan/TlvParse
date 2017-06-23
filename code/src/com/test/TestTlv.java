package com.test;

import com.gmrz.utils.tlv.annotation.TLVMsg;
import com.gmrz.utils.tlv.annotation.TLVType;

@TLVMsg(value=0x2000,type=TLVType.OBJECT)
public class TestTlv {

	@TLVMsg(value=0x2001,type=TLVType.BYTES)
	public byte[] bytes;
	
	@TLVMsg(value=0x2002,type=TLVType.SHORT)
	public short shorts;
	
	@TLVMsg(value=0x1000,type=TLVType.OBJECT)
	public TestObject object;

	@TLVMsg(value=0x2005,type=TLVType.ARRAY)
	public TestObject[] array;
}