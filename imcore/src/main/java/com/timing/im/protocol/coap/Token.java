package com.timing.im.protocol.coap;

import com.timing.im.protocol.core.Bytes;

public class Token extends Bytes {

    public Token(byte[] bytes, int maxLength, boolean copy) {
        super(bytes, maxLength, copy);
    }

    private Token(byte[] token, boolean copy) {
        super(token, 8, copy);
    }

    public static Token fromProvider(byte[] token) {
        return new Token(token, false);
    }

}
