package com.gmrz.utils.tlv.constant;

import com.gmrz.utils.tlv.util.Logger;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Map;

public class ConstantTag {

	public static Map<Short,String> TAGMAP = null;

	public static void setMap(Map<Short,String> tmp){
		TAGMAP = tmp;
	}
	
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
