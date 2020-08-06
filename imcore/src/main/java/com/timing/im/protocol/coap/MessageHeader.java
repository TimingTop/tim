package com.timing.im.protocol.coap;

public class MessageHeader {

    private final int version;
    private final CoAP.Type type;
    private final Token token;
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
