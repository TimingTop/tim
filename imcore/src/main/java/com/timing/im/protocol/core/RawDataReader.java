package com.timing.im.protocol.core;

import java.io.ByteArrayInputStream;

public class RawDataReader {

    private final ByteArrayInputStream byteStream;

    private byte currentByte;
    private int currentBitIndex;

    private byte markByte;
    private int markBitIndex;

    private RawDataReader(final byte[] byteArray) {
        this(byteArray, true);
    }

    private RawDataReader(final byte[] byteArray, boolean copy) {
        byteStream = null;
    }

    public RawDataReader(final ByteArrayInputStream byteStream) {
        this.byteStream = byteStream;

        currentByte = 0;
        currentBitIndex = -1;
        markByte = currentByte;
        markBitIndex = currentBitIndex;
    }

    public byte[] readBytes(final int count) {
        int available = byteStream.available();
        int bytesToRead = count;

        // 如果 少于0，就是代表读取剩余的字节
        if (bytesToRead < 0) {
            bytesToRead = available;
        } else if (bytesToRead > available) {
            throw new IllegalArgumentException("exceeds the available bytes");
        }

        byte[] bytes = new byte[bytesToRead];

        // -1 才是新的字节，如果不是 -1 ，证明当前字节没读完整
        if (currentBitIndex >= 0) {
            // todo： 使用读取 bit 的方法读取 字节，或者 int
            for (int i = 0; i < bytesToRead; i++) {
                bytes[i] = (byte) read(Byte.SIZE);
            }

        } else {
            // 新字节开始读，比较容易
            byteStream.read(bytes, 0, bytes.length);
        }


        return bytes;
    }

    public int readByte() {
        // 读取一个字节
        int val = byteStream.read();
        if (val < 0) {
            throw new IllegalArgumentException("");
        }
        return val;
    }

    private void readCurrentByte() {
        int val = byteStream.read();

        if (val >= 0) {
            currentByte = (byte)val;
        } else {
            throw new IllegalArgumentException("");
        }
        // 初始化
        currentBitIndex = Byte.SIZE - 1;
    }

    public long readLong(final int numBits) {
        long bits = 0;
        // 按照 字节获取
        if (currentBitIndex < 0 && (numBits & 0x7) == 0) {
            for (int i = 0; i < numBits; i += 8) {
                bits <<= 8;
                bits |= readByte();
            }
        } else {
            for (int i = numBits - 1; i >= 0; i--) {
                // 需要读取一个 新 字节
                if (currentBitIndex < 0) {
                    readCurrentByte();
                }
                //    10000000  >> 7  = 00000001   & 1  = 1
                // 判断当前位置有没有 值，有值的话， 要 异或 结果的对应位置，没值的话，就跳过
                boolean bit = (currentByte >> currentBitIndex & 1) != 0;
                if (bit) {
                    bits |= (1L << i);
                }

                --currentBitIndex;
            }
        }
        return bits;
    }


    // same of readLong
    // 用来读取 byte 也行
    public int read(final int numBits) {
        if (numBits < 0 || numBits > Integer.SIZE) {
            throw new IllegalArgumentException("");
        }

        int bits = 0;
        // 判断 bit数 是 8 的倍数，就是按照字节获取
        // 并且是 一个新字节的开始
        if (currentBitIndex < 0 && (numBits & 0x7) == 0) {
            for (int i = 0; i < numBits; i += 8) {
                bits <<=8; // 左移8bit
                bits |= readByte();
            }
        } else {
            for (int i = numBits - 1; i >= 0; i--) {
                // 发现 不够，马上 读下一个 byte 补充上来
                if (currentBitIndex < 0) {
                    readCurrentByte();
                }

                boolean bit = (currentByte >> currentBitIndex & 1) != 0;
                if (bit) {
                    bits |= (1 << i);
                }
                --currentBitIndex;
            }
        }
        return bits;
    }

    public byte readNextByte() {
        byte[] bytes = readBytes(1);
        return bytes[0];
    }

    public byte[] readBytesLeft() {
        return readBytes(-1);
    }

    public boolean bytesAvailable() {
        return byteStream.available() > 0;
    }

    public int available() {
        return byteStream.available();
    }

    /**
     * 判断一下有没有期待的 byte 数可读。
     */
    public boolean bytesAvailable(final int expectedBytes) {
        int left = byteStream.available();
        return left >= expectedBytes;
    }




}
