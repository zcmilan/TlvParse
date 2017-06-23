package com.gmrz.utils.tlv.tlv;

import com.gmrz.utils.tlv.util.Logger;
import com.gmrz.utils.tlv.util.Util;
import com.gmrz.utils.tlv.util.UtilTlv;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangchao on 6/12/17.
 */
public abstract class Msg<T> {

    public short tag = 0;

    public short length = 0;

    public T value = null;

    public byte[] toByte() {
        AkBeanTlv tlv = toBeanTlv();
        return UtilTlv.writeTLV(tlv);
    }

    public void fromByte(byte[] bytes) {
        AkBeanTlv tlv = UtilTlv.readTLV(bytes,(short)0);
        fromBeanTlv(tlv);
    }

    public abstract short getTag();

    public abstract T getValue();

    public abstract Msg<T> setValue(T value);

    public abstract AkBeanTlv toBeanTlv();

    public abstract void fromBeanTlv(AkBeanTlv tlv);

    public abstract static class ByteType extends Msg<byte[]>{

        private static final String TAG = "ByteType";

        @Override
        public byte[] getValue() {
            return value;
        }

        @Override
        public Msg<byte[]> setValue(byte[] value) {
            this.value = value;
            return this;
        }

        @Override
        public AkBeanTlv toBeanTlv() {
            AkBeanTlv tlv = AkBeanTlv.BuildBytes(getTag(),getValue());
            return tlv;
        }

        @Override
        public void fromBeanTlv(AkBeanTlv tlv) {
            setValue(tlv.getValue());
        }
    }

    public abstract static class ShortType extends Msg<Short>{

        private static final String TAG = "ShortType";

        public static final short EMPTY_VALUE = -100;

        public Short value = EMPTY_VALUE;

        @Override
        public Short getValue() {
            return value;
        }

        @Override
        public Msg<Short> setValue(Short value) {
            this.value = value;
            return this;
        }

        @Override
        public AkBeanTlv toBeanTlv() {
            AkBeanTlv tlv = AkBeanTlv.BuildShort(getTag(),getValue());
            return tlv;
        }

        @Override
        public void fromBeanTlv(AkBeanTlv tlv) {
            short tmp = Util.getShort(tlv.getValue(),0);
            setValue(tmp);
        }
    }

    public abstract static class MsgsType extends Msg<Msg[]> {

        private static final String TAG = "MsgsType";

        @Override
        public Msg[] getValue() {
            List<Field> list = getMsgFields();
            Msg[] msgs;
            ArrayList<Msg> result = new ArrayList<Msg>();
            for (int i = 0; i < list.size(); i++) {
                Field field = list.get(i);
                try {
                    Msg msg = (Msg) field.get(this);
                    if(msg != null){
                    	result.add(msg);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if(result.size() > 0){
            	msgs = new Msg[result.size()];
            	result.toArray(msgs);
            }else{
            	msgs = new Msg[0];
            }
            return msgs;
        }

        @Override
        @Deprecated
        public Msg<Msg[]> setValue(Msg[] value) {
            this.value = value;
            return this;
        }

        @Override
        public AkBeanTlv toBeanTlv() {
            AkBeanTlv tlv = new AkBeanTlv();
            tlv.setTag(getTag());
            Msg[] values = getValue();
            AkBeanTlv[] akBeanTlvValues = new AkBeanTlv[values.length];
            for (int i = 0; i < values.length; i++) {
                akBeanTlvValues[i] = values[i].toBeanTlv();
            }
            tlv.setValue(akBeanTlvValues);
            return tlv;
        }

        @Override
        public void fromBeanTlv(AkBeanTlv tlv) {
            AkBeanTlv[] tlvs = tlv.getSubTlv();
            if(tlvs == null){
                Logger.d(TAG,"fromBeanTlv tlv is null");
                return;
            }

            if(tlvs.length == 0){
                Logger.d(TAG,"fromBeanTlv tlv length is 0");
                return;
            }

            Map<Short, AkBeanTlv> tlvMap = new HashMap<>();
            for (int i = 0; i < tlvs.length; i++) {
                AkBeanTlv tmp = tlvs[i];
                tlvMap.put(tmp.getTag(), tmp);
            }
            List<Field> fieldList = getMsgFields();
            for (int i = 0; i < fieldList.size(); i++) {
                Field tmp = fieldList.get(i);
                try {
                    Msg tmpMsg = (Msg) tmp.getType().newInstance();
                    AkBeanTlv tmpTlv = tlvMap.get(tmpMsg.getTag());
                    if(tmpTlv != null){
                        tmpMsg.fromBeanTlv(tmpTlv);
                        tmp.set(this,tmpMsg);
                    }else{
                        Logger.d(TAG,"fromBeanTlv don't have this value :" + tmpMsg.getTag());
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        public List<Field> getMsgFields() {
            Class classz = getClass();
            Field[] fields = classz.getDeclaredFields();
            ArrayList<Field> msgFields = new ArrayList<>();
            for (int i = 0; i < fields.length; i++) {
                Field tmp = fields[i];
                Class tmpClass = tmp.getType();
                Logger.d(TAG,"getMsgFields class:"+tmpClass.getName());
                if (Msg.class.isAssignableFrom(tmpClass)) {
                    Logger.d(TAG,"getMsgFields isassignable from Msg class:"+tmpClass.getName());
                    msgFields.add(tmp);
                }
            }
            return msgFields;
        }

        public MsgsType setValue(String fieldName,short value){
            Class classz = getClass();
            try {
                Field tmp = classz.getDeclaredField(fieldName);
                if(tmp != null){
                    Class tmpClass = tmp.getType();
                    if (Msg.ShortType.class.isAssignableFrom(tmpClass)) {
                        Msg.ShortType tmpMsg = (Msg.ShortType) tmpClass.newInstance();;
                        tmpMsg.setValue(value);
                        tmp.set(this,tmpMsg);
                    }else{
                        Logger.e(TAG,fieldName + " is not short type");
                    }
                }else{
                    Logger.e(TAG,fieldName + " not found field , "+fieldName);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                Logger.e(TAG,fieldName + " NoSuchFieldException , "+fieldName);
            } catch (InstantiationException e) {
                e.printStackTrace();
                Logger.e(TAG,fieldName + " InstantiationException");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Logger.e(TAG,fieldName + " IllegalAccessException");
            }
            return this;
        }

        public MsgsType setValue(String fieldName,byte[] value){
            Class classz = getClass();
            try {
                Field tmp = classz.getDeclaredField(fieldName);
                if(tmp != null){
                    Class tmpClass = tmp.getType();
                    if (Msg.ByteType.class.isAssignableFrom(tmpClass)) {
                        Msg.ByteType tmpMsg = (Msg.ByteType) tmpClass.newInstance();;
                        tmpMsg.setValue(value);
                        tmp.set(this,tmpMsg);
                    }else{
                        Logger.e(TAG,fieldName + " is not bytes type");
                    }
                }else{
                    Logger.e(TAG,fieldName + " not found field , "+fieldName);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                Logger.e(TAG,fieldName + " NoSuchFieldException , "+fieldName);
            } catch (InstantiationException e) {
                e.printStackTrace();
                Logger.e(TAG,fieldName + " InstantiationException");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Logger.e(TAG,fieldName + " IllegalAccessException");
            }
            return this;
        }
    }

    public abstract static class ArrayType extends Msg<Msg[]>{

        private static final String TAG = "ArrayType";

        @Override
        public Msg[] getValue() {
            Logger.d(TAG,"getValue");
            Field field = getArrayField();
            Msg[] result = null;
            try {
            	
            	Object array = field.get(this);
            	if(array != null){
            		int length = Array.getLength(array);
                    Logger.d(TAG,"getValue array length : " + length);
                    result = new Msg[length];
                    for(int i = 0 ; i < length ; ++i) {
                        Object value = Array.get(array, i);
                        if(value instanceof Msg){
                            result[i] = (Msg)value;
                        }else{
                            throw new IllegalArgumentException(this.getClass().getName() + " is not ArrayType");
                        }
                    }
            	}else{
            		Logger.d(TAG, "array is null");
            	}
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        @Deprecated
        public Msg<Msg[]> setValue(Msg[] value) {
            this.value = value;
            return this;
        }

        @Override
        public AkBeanTlv toBeanTlv() {
            Logger.d(TAG,"toBeanTlv");
            AkBeanTlv tlv = new AkBeanTlv();
            tlv.setTag(getTag());
            Msg[] values = getValue();
            AkBeanTlv[] akBeanTlvValues = new AkBeanTlv[values.length];
            for (int i = 0 ; i < values.length ; i++){
                akBeanTlvValues[i] = values[i].toBeanTlv();
            }
            tlv.setValue(akBeanTlvValues);
            return tlv;
        }

        @Override
        public void fromBeanTlv(AkBeanTlv tlv) {
            Logger.d(TAG,"fromBeanTlv");
            Field tmp = getArrayField();
            try {
            	AkBeanTlv[] tmpTlvs = tlv.getSubTlv();
            	if(tmpTlvs != null && tmpTlvs.length > 0){
            		int length = tmpTlvs.length;
            		Class tmpClass = tmp.getType().getComponentType();
                    Msg[] msgs = (Msg[]) Array.newInstance(tmp.getType().getComponentType(),length);
                    AkBeanTlv[] tlvs = tlv.getSubTlv();
                    for (int i=0;i<msgs.length;i++){
                        Msg tmpMsg = (Msg) tmpClass.newInstance();
                        tmpMsg.fromBeanTlv(tlvs[i]);
                        msgs[i] = tmpMsg;
                    }
                    tmp.set(this,msgs);
                    Logger.d(TAG,"set value finish");
            	}
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
				e.printStackTrace();
			}
        }

        public Field getArrayField(){
            Logger.d(TAG,"getArrayField");
            Class classz = getClass();
            Field[] fields = classz.getDeclaredFields();
            for (int i=0;i<fields.length;i++){
                Field tmp = fields[i];
                //check whether is Msg and array , TODO need check
                Class arrayClass = tmp.getType();
                Logger.d(TAG,"getArrayField field : "+tmp.getName() +" className:"+arrayClass.getName());
                if(!arrayClass.isArray()){
                    Logger.d(TAG,"getArrayField is not array");
                    break;
                }
                Logger.d(TAG,"getArrayField is array");
                Class tmpClass = arrayClass.getComponentType();
                if(!Msg.class.isAssignableFrom(tmpClass)){
                    Logger.d(TAG,"getArrayField is not assignable from Msg");
                    break;
                }
                Logger.d(TAG,"getArrayField is assignable from Msg");
                return tmp;
            }
            throw new IllegalArgumentException(this.getClass().getName() + " is not ArrayType");
        }

        public ArrayType setArray(Msg[] m){
            Field tmp = getArrayField();
            if(tmp != null){
                try {
                    Class arrayClass = tmp.getType();
                    Class tmpClass = arrayClass.getComponentType();
                    Class msgClass = m.getClass().getComponentType();
                    if(msgClass == tmpClass){
                        tmp.set(this,m);
                        return this;
                    }
                    if(tmpClass.isAssignableFrom(msgClass)){
                        tmp.set(this,m);
                        return this;
                    }
                    Logger.e(TAG,"type is not fit to array");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }
    }
}
