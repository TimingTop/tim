package com.timing.im.protocol.coap;

public class MessageHeader {

    private final int version;
    private final CoAP.Type type;
    private final Token token;
    // code 能判断是否是 请求，返回等  1byte
    // 0000 0000  =  0.0
    // 0001 1111  =  0.f    代表的是 request
    // 0100 1111 = 0x43  =  2.03  = 203
    // 1110 0011 = 0xe3  =  7.03
    // 前 3 bit ，  后 5 bit ，中间 一个 .
    private final int code;
    private final int mid; // message id, 唯一
    private final int bodyLength;

    public MessageHeader(int version, CoAP.Type type, Token token, int code, int mid, int bodyLength) {
        this.version = version;
        this.type = type;
        this.token = token;
        this.code = code;
        this.mid = mid;
        this.bodyLength = bodyLength;
    }

    public int getVersion() {
        return version;
    }

    public CoAP.Type getType() {
        return type;
    }

    public Token getToken() {
        return token;
    }

    public int getCode() {
        return code;
    }

    public int getMid() {
        return mid;
    }
    // options + payload marker + payload length
    public int getBodyLength() {
        return bodyLength;
    }
}
