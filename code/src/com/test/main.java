package com.test;

import com.gmrz.utils.tlv.constant.ConstantTag;
import com.gmrz.utils.tlv.tlv.Tlv;
import com.gmrz.utils.tlv.util.Logger;
import com.gmrz.utils.tlv.util.UtilByte;

public class main {

	private static final String TAG = "MAIN";
	
    public static void main(String args[]) { 
    	Logger.d(TAG, "START");
    	Tlv tlv = new Tlv();
//    	
//    	short value = tlv.getTag(TestTlv.class);
//    	TLVType type = tlv.getType(TestTlv.class);
//    	Logger.d(TAG, "value:"+value+"  type:"+type);
    	TestTlv test = new TestTlv();
    	test.bytes = new byte[]{0x01,0x02,0x03,0x04,0x05};
    	test.shorts = 1;
    	TestObject subObject = new TestObject();
//    	subObject.bytes = new byte[]{0x06,0x07,0x08,0x09,0x0a};
//    	subObject.shorts = 2;
    	test.object = subObject;
    	TestObject[] subArrayObjects = new TestObject[3];
    	for(int i=0;i<3;i++){
    		TestObject tmpObject = new TestObject();
    		tmpObject.bytes = new byte[]{0x0a,0x0a,0x0a,0x0a,0x0a};
//    		tmpObject.shorts = (short)i;
    		subArrayObjects[i] = tmpObject;
    	}
    	test.array = subArrayObjects;
    	byte[] bytes = tlv.toBytes(test);
    	Logger.d(TAG, UtilByte.byte2base64(bytes));
    	
    	TestTlv test2 = tlv.fromBytes(bytes, TestTlv.class);
    	String json = ByteGson.customGson.toJson(test2);
    	Logger.d(TAG, "convert result :"+json);
    } 
	
}
