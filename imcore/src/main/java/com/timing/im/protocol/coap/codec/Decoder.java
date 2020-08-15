package com.timing.im.protocol.coap.codec;

import com.timing.im.network.endpoint.RawData;
import com.timing.im.protocol.coap.CoAP;
import com.timing.im.protocol.coap.Message;
import com.timing.im.protocol.coap.MessageHeader;
import com.timing.im.protocol.core.RawDataReader;

import java.io.ByteArrayInputStream;

public abstract class Decoder {

    public final Message parseMessage(final RawData raw) {
        if (raw == null) {
            throw new NullPointerException("raw data must not be null");
        }




    }

    public final Message parseMessage(final byte[] raw) {

        RawDataReader reader = new RawDataReader(new ByteArrayInputStream(raw));
        MessageHeader header = parseHeader(reader);

        if (CoAP.isRequest(header.getCode())) {



        } else if (CoAP.isResponse(header.getCode())) {

        } else if (CoAP.isEmptyMessage(header.getCode())) {

        }

    }

    // udp ,  tcp 的协议数据包有不一样的， 只有 头部不一样
    protected abstract MessageHeader parseHeader(RawDataReader reader);
}
