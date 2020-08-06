package com.timing.serialize.core;

import java.io.IOException;
import java.io.OutputStream;

public class FastByteArrayOutputStream extends OutputStream {
    private final FastByteBuffer buffer;

    public FastByteArrayOutputStream() {
        this(1024);
    }

    public FastByteArrayOutputStream(int size) {
        this.buffer = new FastByteBuffer(size);
    }


    @Override
    public void write(int b) throws IOException {
        buffer.append((byte) b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        buffer.append(b, off, len);
    }

    public void reset() {
        buffer.reset();
    }

    public byte[] toByteArray() {
        return buffer.toArray();
    }

    @Override
    public String toString() {
        return new String(toByteArray());
    }
}
