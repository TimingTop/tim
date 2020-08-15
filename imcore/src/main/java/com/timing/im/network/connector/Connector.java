package com.timing.im.network.connector;

import com.timing.im.network.channel.RawDataChannel;

public interface Connector {
    void start() throws Exception;
    void stop();
    void destroy();
    // 代码分层之，  设置 connector 接受 rawData 的channel，有几种
    void setRawDataReceiver(RawDataChannel messageHandler);

}
