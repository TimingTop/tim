package com.timing.im.protocol.coap.core;

import com.timing.im.protocol.coap.Exchange;
import com.timing.im.protocol.coap.Request;

/**
 *
 *  协议无关的 业务pipeline 工具人
 *
 *
 */
public interface Layer {
    void receiveRequest(Exchange exchange, Request request);


    void setLowerLayer(Layer layer);

    void setUpperLayer(Layer layer);
}
