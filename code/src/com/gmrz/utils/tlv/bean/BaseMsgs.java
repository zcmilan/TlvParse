package com.gmrz.utils.tlv.bean;

import com.gmrz.utils.tlv.constant.ConstantTag;
import com.gmrz.utils.tlv.tlv.Msg;

/**
 * Created by zhangchao on 6/12/17.
 */
public class BaseMsgs {

    public static class CERT_USERNAME extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_USERNAME;
        }
    }

    public static class CERT_APPID extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_APPID;
        }
    }


    public static class CERT_KEYID extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_KEYID;
        }
    }

    public static class CERT_FINALCHALLENGE extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_FINALCHALLENGE;
        }
    }

    public static class CERT_KHACCESSTOKEN extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_KHACCESSTOKEN;
        }
    }

    public static class CERT_AUTHTYPE extends Msg.MsgsType.ShortType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_AUTHTYPE;
        }
    }

    public static class CERT_USERVERIFY_TOKEN extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_USERVERIFY_TOKEN;
        }
    }

    public static class CERT_FINGER_TYPE extends Msg.MsgsType.ShortType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_FINGER_TYPE;
        }
    }

    public static class CERT_ALG extends Msg.MsgsType.ShortType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_ALG;
        }
    }

    public static class CERT_REQ_INFO extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_REQ_INFO;
        }
    }

    public static class CERT_USERCERT_INFO extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_USERCERT_INFO;
        }
    }

    public static class CERT_REQ_INFO_SIGN extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_REQ_INFO_SIGN;
        }
    }

    public static class CERT_REQ_SIGNDATA_SIGN extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_REQ_SIGNDATA_SIGN;
        }
    }


    public static class CERT_DEVICE_CERT extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_DEVICE_CERT;
        }
    }

    public static class CERT_COUNTER extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_COUNTER;
        }
    }

    public static class CERT_TRANSCATIONCONTENT extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_TRANSCATIONCONTENT;
        }
    }

    public static class CERT_TRANSCATIONCONTENT_HASH extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_TRANSCATIONCONTENT_HASH;
        }
    }

    public static class CERT_TRANS_INFO_SIGN extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_TRANS_INFO_SIGN;
        }
    }


    public static class CERT_DEVICE_ID extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_DEVICE_ID;
        }
    }

    public static class CERT_SW extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_SW;
        }
    }

    public static class CERT_USERVERIFY_SET extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_USERVERIFY_SET;
        }
    }

    public static class CERT_PIN_BLOCK extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_PIN_BLOCK;
        }
    }

    public static class CERT_UVI extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CER_UVI;
        }
    }

    public static class CERT_UVS extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_UVS;
        }
    }

    public static class CERT_STATUS_CODE extends Msg.MsgsType.ShortType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_STATUS_CODE;
        }
    }

    public static class CERT_PUBKEY extends Msg.MsgsType.ByteType{

        @Override
        public short getTag() {
            return ConstantTag.TAG_CERT_PUBKEY;
        }
    }
}
