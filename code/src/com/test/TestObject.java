package com.test;

import com.gmrz.utils.tlv.annotation.TLVMsg;
import com.gmrz.utils.tlv.annotation.TLVType;

@TLVMsg(value=0x1000,type=TLVType.OBJECT)
public class TestObject {
	
	@TLVMsg(value=0x1001,type=TLVType.BYTES)
	public byte[] bytes;
	
//	@TLVMsg(value=0x1002,type=TLVType.SHORT)
//	public short shorts;
}
