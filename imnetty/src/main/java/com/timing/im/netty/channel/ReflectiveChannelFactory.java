package com.timing.im.netty.channel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

// 根据传入的 channel 生成反射工厂
public class ReflectiveChannelFactory<T extends Channel> implements ChannelFactory<T> {

    private final Constructor<? extends T> constructor;

    public ReflectiveChannelFactory(Class<? extends T> clazz) {

        try {
            this.constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("error");
        }
    }

    @Override
    public T newChannel() {

        try {
            return constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("error");
        }
    }
}
