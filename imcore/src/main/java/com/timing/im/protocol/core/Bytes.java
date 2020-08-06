package com.timing.im.protocol.core;

import java.util.Arrays;

public class Bytes {

    public static final byte[] EMPTY = new byte[0];
    // 存储 key
    private final byte[] bytes;
    private final int hash;
    private String asString;

    public Bytes(byte[] bytes, int maxLength, boolean copy) {
        this.bytes = copy? Arrays.copyOf(bytes, bytes.length) : bytes;
        this.hash = Arrays.hashCode(bytes);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Bytes other = (Bytes) obj;
        if (hash != other.hash) return false;

        return Arrays.equals(bytes, other.bytes);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public final byte[] getBytes() {
        return bytes;
    }

    public final String getAsString() {
        if (asString == null) {

        }
        return asString;
    }
}
