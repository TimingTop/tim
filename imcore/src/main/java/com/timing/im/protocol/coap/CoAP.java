package com.timing.im.protocol.coap;

public class CoAP {

    public enum Type {
        CON(0),//确认
        NON(1),// 不确认
        ACK(2),// ack
        RET(3);// reject

        public final int value;
        Type(int value) {
            this.value = value;
        }

        public static Type valueOf(final int value) {
            switch (value) {
                case 0: return CON;
                case 1: return NON;
                case 2: return ACK;
                case 3: return RET;
                default: throw new IllegalArgumentException("Unknown CoAP type " + value);
            }
        }
    }

    public final class MessageFormat {
        public static final int VERSION_BITS = 2;
        public static final int TYPE_BITS = 2;
        public static final int TOKEN_LENGTH_BITS = 4;
        public static final int CODE_BITS = 8;
        public static final int MESSAGE_ID_BITS = 16;
        public static final int OPTION_DELTA_BITS = 4;
        public static final int OPTION_LENGTH_BITS = 4;
        public static final byte PAYLOAD_MARKER = (byte) 0xFF;

        public static final int VERSION = 1;
        // code 8 bit
        public static final int EMPTY_CODE = 0b00000000; //0 00 标识是空message，用二进制标识
    }
}
