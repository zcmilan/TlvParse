package com.gmrz.utils.tlv.tlv;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.gmrz.utils.tlv.annotation.TLVMsg;
import com.gmrz.utils.tlv.annotation.TLVType;
import com.gmrz.utils.tlv.util.Logger;
import com.gmrz.utils.tlv.util.UtilByte;

public class AnnotationParse {
	
	private static final String TAG = "Tlv2";
	
	public AkBeanTlv toAkBeanTlv(Object o){
		if(o == null){
			return null;
		}
        if(isTLVMsg(o.getClass())){
        	Logger.d(TAG, "class name:"+o.getClass().getName());
            AkBeanTlv tlv = new AkBeanTlv();
            short topTag = getTag(o.getClass());
            if(topTag == -1){
            	return null;
            }
            tlv.setTag(topTag);
            Field[] fields = o.getClass().getDeclaredFields();
            ArrayList<AkBeanTlv> subTlvs = new ArrayList<>();
            outer : for(int i=0;i<fields.length;i++){
                Field tmpField = fields[i];
                Logger.d(TAG, "field name:"+tmpField.getName());
                if(!isTLVMsg(tmpField)){
                	Logger.d(TAG, "is not tmpField ");
                	continue;
                }
                short tag = getTag(tmpField);
                if(tag == -1){
                	Logger.d(TAG, "not set tag , tag:"+tag);
                	continue;
                }
                TLVType type = getType(tmpField);
                if(type == null){
                	Logger.d(TAG, "not set type ");
                	continue;
                }
                AkBeanTlv tmpTlv = new AkBeanTlv();
                tmpTlv.setTag(tag);
                switch(type){
                case BYTES:
                	tmpTlv = getBytesValue(tmpField, o, tmpTlv);
    				break;
    			case SHORT:
    				tmpTlv = getShortValue(tmpField, o, tmpTlv);
    				break;
    			case OBJECT:
    				tmpTlv = getObjectValue(tmpField, o, tmpTlv);
    				break;
    			case ARRAY:
    				tmpTlv = getArrayValue(tmpField, o, tmpTlv);
    				break;
    			default:
    				Logger.e(TAG,"type not know");
                }
                if(tmpTlv == null){
                	Logger.d(TAG,"tmpTlv is null");
                	continue;
                }
                subTlvs.add(tmpTlv);
            }
            AkBeanTlv[] tlvsArray = new AkBeanTlv[subTlvs.size()];
            for(int i=0;i<subTlvs.size();i++){
                tlvsArray[i] = subTlvs.get(i);
            }
            tlv.setValue(tlvsArray);
            return tlv;
        }
        return null;
    }

	
	public <T> T fromAkBeanTlv(AkBeanTlv tlv,Class<T> classz){
		Logger.d(TAG, "fromAkBeanTlv");
		if(tlv == null){
			Logger.d(TAG, "fromAkBeanTlv tlv is null");
			return null;
		}
		if(tlv.getSubTlv() == null || tlv.getSubTlv().length <= 0){
			Logger.d(TAG, "fromAkBeanTlv subtlv is null");
			return null;
		}
		Map<Short,AkBeanTlv> tlvMap = new Hashtable<>();
		for(int i=0;i<tlv.getSubTlv().length;i++){
			AkBeanTlv tmpTlv = tlv.getSubTlv()[i];
			tlvMap.put(tmpTlv.getTag(), tmpTlv);
		}
		
		T result = null;
		if(classz == null){
			Logger.d(TAG, "fromAkBeanTlv classz is null");
			return null;
		}
		if(!isTLVMsg(classz)){
			Logger.d(TAG, "fromAkBeanTlv is TLVMsg");
			return null;
		}
		try {
			result = classz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result == null){
			Logger.d(TAG, "fromAkBeanTlv new class failed ,class:"+classz.getName());
			return null;
		}
		short topTag = getTag(classz);
		if(topTag != tlv.getTag()){
			Logger.d(TAG, "fromAkBeanTlv get top tag failed");
			return null;
		}
		Field[] fields = classz.getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			Field tmpField = fields[i];
			Logger.d(TAG, "fromAkBeanTlv tmpField :"+tmpField.getName());
			if(!isTLVMsg(tmpField)){
				Logger.d(TAG, "fromAkBeanTlv ");
				continue;
			}
			short tmpFieldValue = getTag(tmpField);
			if(tmpFieldValue == -1){
				Logger.d(TAG, "fromAkBeanTlv tmpFieldValue is not set");
				continue;
			}
			AkBeanTlv tmpTlv = tlvMap.get(tmpFieldValue);
			if(tmpTlv == null){
				Logger.d(TAG, "fromAkBeanTlv akBeanTlv not has this sub:"+tmpFieldValue);
				continue;
			}
			TLVType tmpFieldType = getType(tmpField);
			if(tmpFieldType == null){
				Logger.d(TAG, "fromAkBeanTlv get type failed");
				continue;
			}
			switch(tmpFieldType){
			case BYTES:
				setBytesValue(tmpField,result,tmpTlv);
				break;
			case SHORT:
				setShortValue(tmpField,result,tmpTlv);
				break;
			case OBJECT:
				setObjectValue(tmpField,result,tmpTlv);
				break;
			case ARRAY:
				setArrayValue(tmpField,result,tmpTlv);
				break;
			default:		
			}
		}
		return result;
	}
	
	public boolean isTLVMsg(Class classz){
		if(!classz.isAnnotationPresent(TLVMsg.class)){
			return false;
		}
		return true;
	}
	
	public boolean isTLVMsg(Field classz){
		if(!classz.isAnnotationPresent(TLVMsg.class)){
			return false;
		}
		return true;
	}
	
	public short getTag(Class classz){
		if(!isTLVMsg(classz)){
			return -1;
		}
		TLVMsg tlv = (TLVMsg)classz.getAnnotation(TLVMsg.class);
		short value = tlv.value();
		Logger.d(TAG, String.format("getTag value:%x", value));
		return value;
	}

	public TLVType getType(Class classz){
		if(!isTLVMsg(classz)){
			return null	;
		}
		TLVMsg tlv = (TLVMsg)classz.getAnnotation(TLVMsg.class);
		Logger.d(TAG, "getType value:"+tlv.type());
		return tlv.type();
	}
	
	public short getTag(Field classz){
		if(!isTLVMsg(classz)){
			return -1;
		}
		TLVMsg tlv = (TLVMsg)classz.getAnnotation(TLVMsg.class);
		short value = tlv.value();
		Logger.d(TAG, String.format("getTag value:%x", value));
		return value;
	}

	public TLVType getType(Field classz){
		if(!isTLVMsg(classz)){
			return null	;
		}
		TLVMsg tlv = (TLVMsg)classz.getAnnotation(TLVMsg.class);
		Logger.d(TAG, "getType value:"+tlv.type());
		return tlv.type();
	}

	public void setShortValue(Field field,Object object,AkBeanTlv value){
		Logger.d(TAG, "setShortValue");
		short shortValue = UtilByte.getShort(value.getValue(), 0);
		try {
			field.set(object, shortValue);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public AkBeanTlv getShortValue(Field field,Object object,AkBeanTlv value){
		try {
			short shortValue = field.getShort(object);
			value.setValue(shortValue);
			return value;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
	public void setBytesValue(Field field,Object object,AkBeanTlv value){
		Logger.d(TAG, "setBytesValue field :"+field.getName());
		byte[] bytes = value.getValue();
		try {
			field.set(object, bytes);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public AkBeanTlv getBytesValue(Field field,Object object,AkBeanTlv value){
		try {
			Logger.d(TAG, "getBytesValue field:"+field.getName());
			byte[] bytesValue = (byte[])field.get(object);
			value.setValue(bytesValue);
			return value;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
	public void setObjectValue(Field field,Object object,AkBeanTlv value){
		Logger.d(TAG, "setObjectValue");
		Object subObject = fromAkBeanTlv(value, field.getType());
		try {
			field.set(object, subObject);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public AkBeanTlv getObjectValue(Field field,Object object,AkBeanTlv needTlv){
		Logger.d(TAG, "getObjectValue");
		try {
			Object objectValue = field.get(object);
			if(objectValue == null){
				Logger.d(TAG, "getObjectValue return null");
				return null;
			}
			AkBeanTlv tlv = toAkBeanTlv(objectValue);
			if(needTlv.getTag() != tlv.getTag()){
				Logger.d(TAG, "getObjectValue tag not fit,needTlv:"+needTlv.getTag()+" tlv:"+tlv.getTag());
				return null;
			}
			return tlv;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setArrayValue(Field field,Object object,AkBeanTlv value){
		Logger.d(TAG, "setArrayValue");
		if(value == null){
			Logger.d(TAG, "setArrayValue value is null");
			return;
		}
		if(value.getSubTlv() == null || value.getSubTlv().length <= 0){
			Logger.d(TAG, "setArrayValue has not subtlvs");
			return;
		}
		int length = value.getSubTlv().length;
		
		Class classz = field.getType();
		if(!classz.isArray()){
			Logger.d(TAG, "setArrayValue classz is not array");
			return;
		}

		Class subClass = classz.getComponentType();
        if(!isTLVMsg(subClass)){
        	Logger.d(TAG, "setArrayValue subClass is not subclass");
        	return;
        }
        
		Object[] msgs = (Object[]) Array.newInstance(subClass,length);
        AkBeanTlv[] tlvs = value.getSubTlv();
        for (int i=0;i<msgs.length;i++){
        	Object tmpMsg = fromAkBeanTlv(tlvs[i], subClass);
            msgs[i] = tmpMsg;
        }
        try {
			field.set(object,msgs);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public AkBeanTlv getArrayValue(Field field,Object object,AkBeanTlv needTlv){
		Logger.d(TAG,"getArrayValue");
		if(!field.getType().isArray()){
			Logger.d(TAG,"getArrayValue is not array");
			return null;
		}
		if(needTlv == null){
			Logger.d(TAG,"getArrayValue needTlv is null");
			return null;
		}
		Object arrayObject = null;
		try {
			arrayObject = field.get(object);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(arrayObject == null){
			Logger.d(TAG,"getArrayValue get array Object null");
			return null;
		}
		Class arrayClass = field.getType();
		Class componentClass = arrayClass.getComponentType();
		if(!isTLVMsg(componentClass)){
			Logger.d(TAG,"getArrayValue component is not TLVMsg");
			return null;
		}
		int length = Array.getLength(arrayObject);
		Logger.d(TAG,"getArrayValue length is :"+length);
		AkBeanTlv[] subTlvs = new AkBeanTlv[length];

		for(int i=0;i<length;i++){
			Object tmpObject = Array.get(arrayObject, i);
			AkBeanTlv tlv = toAkBeanTlv(tmpObject);
			subTlvs[i] = tlv;
		}
		needTlv.setValue(subTlvs);
		return needTlv;
	}
}
