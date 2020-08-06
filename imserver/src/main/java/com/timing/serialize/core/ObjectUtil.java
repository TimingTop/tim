package com.timing.serialize.core;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectUtil {

    public static <T> byte[] serialize(T obj) {
        if (null != obj && obj instanceof Serializable) {

            FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
            ObjectOutputStream oos = null;

            try {
                oos = new ObjectOutputStream(byteOut);
                oos.writeObject(obj);
                oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != oos) {
                    try {
                        oos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return byteOut.toByteArray();
        }

        return null;
    }

    public static <T> T unserialize(byte[] bytes) {
        ObjectInputStream ois = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
