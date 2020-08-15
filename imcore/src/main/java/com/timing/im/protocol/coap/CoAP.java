package com.timing.im.protocol.coap;

public class CoAP {

    public static final int VERSION = 0x01;
    public static final String PROTOCOL_TCP = "TCP";
    public static final String PROTOCOL_UDP = "UDP";

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
        // tcp 协议相关
        public static final int LENGTH_NIBBLE_BITS = 4;

        // udp 协议相关
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

    // 根据 code 的值来判断是否 request or response
    public static boolean isRequest(final int code) {
        // 如果是 response 的话，  code 的二进制格式如下：
        // 0010 0000  = 1.0 = 100 开始往后面算
        // 0100 0000  = 2.0 = 200
        // 所以 request 排除之后 的值
        // 0000 0001  ~ 0001 1111
        return code >= 0b00000001 &&
               code <= 0b00011111;
    }
    // empty message     0.00
    // 协议规范 request    0.01 - 0.31
    // response          2.00 - 5.31
    public static boolean isResponse(final int code) {
        return code >= 0b01000000 &&
               code <= 0b10100000;
    }

    public static boolean isEmptyMessage(final int code) {
        return code == 0b00000000;
    }
}
