package com.timing.im.container;

import com.timing.im.IMServer;

public interface Module {
    String getName();
    void initialize(IMServer server);
    void start();
    void stop();
    void destroy();
}
