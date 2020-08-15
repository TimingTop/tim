package com.timing.im.protocol.coap.core;

import com.timing.im.protocol.coap.Exchange;
import com.timing.im.protocol.coap.Request;

import java.util.concurrent.ExecutorService;

public class AbstractLayer implements Layer {

    // 两个是 同一个 实例
    private Layer upperLayer = LogOnlyLayer.getInstance();
    private Layer lowerLayer = LogOnlyLayer.getInstance();

    protected ExecutorService executor;

    @Override
    public void receiveRequest(Exchange exchange, Request request) {

    }

    @Override
    public void setLowerLayer(Layer layer) {
        if (lowerLayer != layer) {
            lowerLayer = layer;
            lowerLayer.setUpperLayer(this);
        }
    }

    @Override
    public void setUpperLayer(Layer layer) {
        if (upperLayer != layer) {

            upperLayer = layer;
            upperLayer.setLowerLayer(this);
        }
    }

    public static final class LogOnlyLayer implements Layer {
        private static final LogOnlyLayer INSTANT = new LogOnlyLayer();
        private LogOnlyLayer() {}
        public static LogOnlyLayer getInstance() {
            return INSTANT;
        }
        @Override
        public void receiveRequest(Exchange exchange, Request request) {

        }

        // 这个是最开始 或者 最底层的类
        @Override
        public void setLowerLayer(Layer layer) {
            // doNothing
        }

        @Override
        public void setUpperLayer(Layer layer) {
            // doNothing
        }
    }
}
