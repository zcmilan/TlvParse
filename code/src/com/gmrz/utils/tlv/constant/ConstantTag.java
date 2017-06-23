package com.gmrz.utils.tlv.constant;

import com.gmrz.utils.tlv.util.Logger;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Map;

public class ConstantTag {

	//Request
	public static final short TAG_CERT_REG_CMD = 0X5401;
	public static final short TAG_CERT_AUTH_CMD = 0X5402;
	public static final short TAG_CERT_DEREG_CMD = 0X5403;
	public static final short TAG_CERT_STORE_CMD = 0X5404;
	public static final short TAG_CERT_QUERY_CMD = 0X5405;
	public static final short TAG_CERT_GETDEVICE_INFO_CMD = 0X5407;
	public static final short TAG_CERT_GET_REGINFO = 0X5406;

	//Response
	public static final short TAG_CERT_REG_CMD_RESPONSE = 0X5601;
	public static final short TAG_CERT_AUTH_CMD_RESPONSE = 0X5602;
	public static final short TAG_CERT_DEREG_CMD_RESPONSE = 0X5603;
	public static final short TAG_CERT_STORE_CMD_RESPONSE = 0X5604;
	public static final short TAG_CERT_QUERY_CMD_RESPONSE = 0X5605;
	public static final short TAG_CERT_GET_REGINFO_RESPONSE = 0X5606;
	public static final short TAG_CERT_GETDEVICE_INFO_CMD_RESPONSE = 0X5607;


	//TAGS
	public static final short TAG_CERT_USERNAME	= 0X5201; //username
	public static final short TAG_CERT_APPID	=	0X5202;	//appID
	public static final short TAG_CERT_FINALCHALLENGE	=	0X5203;//final challenge
	public static final short TAG_CERT_KHACCESSTOKEN	=	0X5204;//Kh access token
	public static final short TAG_CERT_AUTHTYPE	=	0X5205;//Auth type , can be Finger or PIN
	public static final short TAG_CERT_USERVERIFY_TOKEN	=	0X5206;//UVT
	public static final short TAG_CERT_FINGER_TYPE	=	0X5207;	//FingerType , can be UVI|UVS
	public static final short TAG_CERT_PIN_BLOCK = 0X5208;//PIN uvt
	public static final short TAG_CERT_ALG	=	0X5209;//alg , sm2 is ??? TODO
	public static final short TAG_CERT_USERCERT_INFO	=	0X520A;//X509 CER for user , signed by CA
	public static final short TAG_CERT_TRANSCATIONCONTENT	=	0x5211;//transaction content
	public static final short TAG_CERT_TRANSCATIONCONTENT_HASH	=	0X5212;//transaction content hash
	public static final short TAG_CERT_USERVERIFY_SET = 0X5214;//uvs , ASM send it to ak
	public static final short TAG_CER_UVI = 0X5215;//uvi , ak send back to server
	public static final short TAG_CERT_UVS = 0X5216;//uvs , ak send back to server
	public static final short TAG_CERT_PUBKEY = 0X5217;//pubkey , get in register , used to calculate p10
	public static final short TAG_CERT_REQ_INFO	=	0x521E;//using in register , p10 structure , will be create before register request by ASM

	public static final short TAG_CERT_STATUS_CODE = 0X5301;//status code
	public static final short TAG_CERT_ASSRTION_INFO	=	0X5302;//assertion info , for register / authenticate / getRegrations
	public static final short TAG_CERT_TRANS_INFO_SIGN	=	0X5303;//signature for transaction , TODO need modify by huyl@gmrz-bj.com
	public static final short TAG_CERT_DEVICE_ID	=	0X5304;//device id
	public static final short TAG_CERT_SW	=	0X5305;//cer version


	public static final short TAG_CERT_REQ_INFO_SIGN	=	0X5501;//using in register , p10 structure , will be return from AK register , and ASM need to replace with p10 last request
	public static final short TAG_CERT_REQ_SIGN_DATA	=	0X5502;//using in register , data which ready to sign
	public static final short TAG_CERT_REQ_SIGNDATA_SIGN	=	0X5503;//using in register
	public static final short TAG_CERT_DEVICE_CERT	=	0x5504;//using in register , device cert and will be check by server with metadata
	public static final short TAG_CERT_COUNTER	=	0X5505;//counter , first 2 bytes are register counter , last 2 bytes are auth counter
	public static final short TAG_CERT_KEYID	=	0X5506;//KEYID
	public static final short TAG_CERT_AUTH_SIGN_DATA = 0X5507;//Auth data ready to sign
	public static final short TAG_CERT_REG_INFO = 0X5509;//REG INFO
	
	public static final short TAG_TEST_2_ =0X1000;
	public static final short TAG_TEST_2_1 =0X1001;
	public static final short TAG_TEST_2_2 =0X1002;
	public static final short TAG_TEST_1_ALL =0X2000;
	public static final short TAG_TEST_1_1 =0X2001;
	public static final short TAG_TEST_1_2 =0X2002;
	public static final short TAG_TEST_1_3 =0X2003;
	public static final short TAG_TEST_1_4 =0X2005;
	
	
	public static Map<Short,String> TAGMAP = null;
	
	static{
		TAGMAP = new Hashtable<Short, String>();
		Field[] fields = ConstantTag.class.getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			Field tmp = fields[i];
			String tag = tmp.getName();
			if(tag.contains("TAG_")){
				short value;
				try {
					value = tmp.getShort(null);
					TAGMAP.put(value,tag);	
				} catch (IllegalArgumentException e) {
					Logger.e("ConstantTag","IllegalArgumentException:"+e.getMessage());
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					Logger.e("ConstantTag","IllegalAccessException:"+e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

}
