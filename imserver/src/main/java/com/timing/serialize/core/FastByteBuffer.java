package com.timing.serialize.core;

/**
 *
 * 构建一个 byte 的二维数组 的 缓冲区
 *
 * 因为是 大内存，可重复使用，减少 GC 频率就能提高性能
 *
 *
 */
public class FastByteBuffer {

    private byte[][] buffers = new byte[16][];
    private int buffersCount;
    // 当前 第几行
    private int currentBufferIndex = -1;
    // 指向的是 当前缓冲行
    private byte[] currentBuffer;
    // 指向的是 当前缓冲行 已经使用的 字节数
    private int offset;
    // 整个二维缓冲区 使用的 字节数
    private int size;
    // 二维数组 每一行的 长度，默认是 1024，可自动扩张
    private final int minChunkLen;

    public FastByteBuffer() {
        this.minChunkLen = 1024;
    }

    public FastByteBuffer(int size) {
        this.minChunkLen = size;
    }

    private void needNewBuffer(int newSize) {
        int delta = newSize - this.size;
        int newBufferSize = Math.max(this.minChunkLen, delta);
        ++this.currentBufferIndex;
        this.currentBuffer = new byte[newBufferSize];
        this.offset = 0;

        // 如果 需要的行数  >= 当前行数，就要扩容
        // * 2 倍数 扩容
        if (this.currentBufferIndex >= this.buffers.length) {
           int newLen = this.buffers.length << 1;
           byte[][] newBuffers = new byte[newLen][];
           // 把 旧的数组 整个 复制 到 新数组中
           System.arraycopy(this.buffers, 0, newBuffers, 0, this.buffers.length);
           this.buffers = newBuffers;
        }

        this.buffers[this.currentBufferIndex] = this.currentBuffer;
        // 记录申请了缓存区的 次数
        ++this.buffersCount;
    }

    public FastByteBuffer append(byte[] array, int off, int len) {
        int end = off + len;
        if (off >= 0 && len >= 0 && end <= array.length) {
            if (len == 0) {
                return this;
            } else {
                // 因为 每一行 有部分 byte 可能被用，所以要算出 最大值
                // 有可能会 超过 一行 的长度，
                int newSize = this.size + len;
                // 还剩多少  的 字节 还没放
                int remaining = len;
                int part;
                if (this.currentBuffer != null) {
                    // 当前缓冲行 已经被 使用了一部分

                    part = Math.min(len, this.currentBuffer.length - offset);
                    System.arraycopy(array, end - len, this.currentBuffer, this.offset, part);
                    // 求出 还剩多少 字节还没放，如果 = 0 ，就是所以字节都放了
                    remaining = len - part;
                    this.offset += part;
                    this.size += part;
                }
                // 如果 remaining 不= 0 ，还剩字节没放
                if (remaining > 0) {
                    // 用 newSize 会越来越大的
                    // 但是 代码里面会减掉，最好 扩充的 就是 len
                    // 这个 len 是系统IO缓存的长度，
                    // len 是需要写入的 字节数，扩充的长度是 len
                    // 因为上一行已经写入了部分 字节，所以，当前行肯定是足够存放此 字节数组。
                    this.needNewBuffer(newSize);
                    //  恒 等于  （remaining， len-0）,返回肯定是 remaining
                    part = Math.min(remaining, this.currentBuffer.length - this.offset);
                    this.offset += part;
                    this.size += part;
                }
                return this;
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public FastByteBuffer append(byte[] array) {
        return this.append(array, 0, array.length);
    }

    public FastByteBuffer append(byte element) {
        if (this.currentBuffer == null || this.offset == this.currentBuffer.length) {
            // 刚好到 需要扩容的
            this.needNewBuffer(this.size + 1);
        }
        this.currentBuffer[this.offset] = element;
        ++this.offset;
        ++this.size;
        return this;
    }

    // 合并 两个 FastByteBuffer
    public FastByteBuffer append(FastByteBuffer buff) {
        if (buff.size == 0) {
            return this;
        } else {
            for (int i = 0; i < buff.currentBufferIndex; i++) {
                this.append(buff.buffers[i]);
            }
            this.append(buff.currentBuffer, 0 , buff.offset);
            return this;
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int index() {
        return this.currentBufferIndex;
    }
    public int offset() {
        return this.offset;
    }

    public byte[] array(int index) {
        return this.buffers[index];
    }

    public void reset() {
        this.size = 0;
        this.offset = 0;
        this.currentBufferIndex = -1;
        this.currentBuffer = null;
        this.buffersCount = 0;
    }

    public byte[] toArray() {
        int pos = 0;
        byte[] array = new byte[this.size];
        if (this.currentBufferIndex == -1) {
            return array;
        } else {
            for (int i = 0; i < this.currentBufferIndex; i++) {
                int len = this.buffers[i].length;
                // 把没一行 都 复制到 一个 数组中
                System.arraycopy(this.buffers[i], 0, array, pos, len);
                pos += len;
            }

            System.arraycopy(this.buffers[this.currentBufferIndex], 0, array, pos, this.offset);
            return array;
        }
    }

    public byte[] toArray(int start, int len) {
        int remaining = len;
        int pos = 0;
        byte[] array = new byte[len];
        if (len == 0) {
            return array;
        } else {
            int i;
            for (i = 0; start >= this.buffers[i].length; i++) {
                start -= this.buffers[i].length;
            }

            while (i < this.buffersCount) {
                byte[] buf = this.buffers[i];
                int c = Math.min(buf.length - start, remaining);
                System.arraycopy(buf, start, array, pos, c);
                pos += c;
                remaining -= c;
                if (remaining == 0) {
                    break;
                }
                start = 0;
                i++;
            }

            return array;
        }
    }

}
