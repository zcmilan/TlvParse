package com.gmrz.utils.tlv.tlv;

/**
 * Created by zhangchao on 6/15/17.
 */
public class MsgBuilder {

    public static <T extends Msg> T Build(Class<T> classz){
        try {
            T instance = classz.newInstance();
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
