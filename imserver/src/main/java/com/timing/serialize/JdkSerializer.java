package com.timing.serialize;

import com.sun.tools.javac.util.Assert;

public class JdkSerializer implements Serializer {

    private JdkSerializer() {}

    public byte[] serialize(Object object) {
        Assert.checkNonNull(object, "object can not be null");

        return new byte[0];
    }

    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return null;
    }
}
