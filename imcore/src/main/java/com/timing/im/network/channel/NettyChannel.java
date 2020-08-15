package com.timing.im.network.channel;

import com.timing.im.network.endpoint.RawData;

import java.util.ArrayList;
import java.util.List;

public class NettyChannel implements RawDataChannel {

    private final List<RawData> messages = new ArrayList<>();
    private final Object lock = new Object();

    @Override
    public void receiveData(RawData rawData) {
        synchronized (lock) {
            messages.add(rawData);
            lock.notifyAll();
        }
    }
}
