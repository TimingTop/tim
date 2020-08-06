package com.timing.im.network.connector;

import java.net.SocketException;

public interface Connector {
    void start() throws Exception;
    void stop();
    void destroy();
}
