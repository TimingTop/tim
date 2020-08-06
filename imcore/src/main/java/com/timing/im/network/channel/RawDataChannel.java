package com.timing.im.network.channel;

import com.timing.im.network.endpoint.RawData;

public interface RawDataChannel {

    void receiveData(RawData rawData);
}
