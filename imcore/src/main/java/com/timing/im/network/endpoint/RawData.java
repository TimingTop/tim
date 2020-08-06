package com.timing.im.network.endpoint;

/**
 *
 * tcp or  udp 用到的读取到的二进制包，
 * 准备进入  协议解析
 *
 */
public class RawData {
    // 数据包的 二进制对象
    public final byte[] bytes;
    // 接受数据的时间
    private final long receiveNanoTimestamp;

    private RawData(byte[] data, long nanoTimestamp) {
        this.bytes = data;
        this.receiveNanoTimestamp = nanoTimestamp;
    }

    public static RawData inbound(byte[] data, long nanoTimestamp) {
        return new RawData(data, nanoTimestamp);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public long getReceiveNanoTimestamp() {
        return receiveNanoTimestamp;
    }
}
