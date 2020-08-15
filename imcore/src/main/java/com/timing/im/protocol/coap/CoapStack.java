package com.timing.im.protocol.coap;

/***
 *
 * 协议栈，是业务相关的东西，
 *
 * 做成 pipeline，
 *
 *
 *
 */
public interface CoapStack {

    void receiveRequest(Exchange exchange, Request request);
}
