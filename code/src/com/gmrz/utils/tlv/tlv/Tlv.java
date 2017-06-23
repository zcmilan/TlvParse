package com.gmrz.utils.tlv.tlv;

import com.gmrz.utils.tlv.util.Logger;
import com.gmrz.utils.tlv.util.UtilTlv;

/**
 * Created by zhangchao on 6/12/17.
 * add to support parse tlv liking Gson
 */
public class Tlv {

    private static final String TAG = "Tlv";

    public byte[] toBytes(Msg msg){
        if(msg == null){
            Logger.e(TAG,"toBytes error , msg is null");
            return null;
        }
        return msg.toByte();
    }

    public Msg fromBytes(byte[] bytes,Class classz){
        Msg msg = null;
        if(classz == null){
            Logger.e(TAG,"fromBytes error , classz is null");
            return null;
        }
        if(!Msg.class.isAssignableFrom(classz)){
            Logger.e(TAG,"fromBytes error , classz is not Msg type");
            return null;
        }
        if(bytes == null || bytes.length == 0){
            Logger.e(TAG,"fromBytes error , bytes is null or empty");
            return null;
        }
        try {
            msg = (Msg)classz.newInstance();
            msg.fromByte(bytes);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Logger.d(TAG,"frombytes finish msg:"+msg);
        return msg;
    }

}
