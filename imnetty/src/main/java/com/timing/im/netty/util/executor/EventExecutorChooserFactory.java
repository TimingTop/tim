package com.timing.im.netty.util.executor;

public interface EventExecutorChooserFactory {

    EventExecutorChooser newChooser(EventExecutor[] executors);

    interface EventExecutorChooser {
        EventExecutor next();
    }
}
