package com.timing.im.protocol.packet;

import java.io.Serializable;

/**
 * Packet 协议的详细格式
 *
 * 1 byte :  magic number    0xBC
 * 1 byte ： serialize algorithm
 * 1 byte :  message type  1:reg  2:res 3:cmd
 * 4 bytes : content length   内容的长度
 * N bytes : content   内容，可以是序列化的队形字节数组
 *
 *
 *
 */
public abstract class Packet implements Serializable {

    public static byte MAGIC_NUMBER = (byte) 0xBC;

    private byte magic = MAGIC_NUMBER;
    private long id;
    // 1:request 2:response 3:command
    private byte type;




    private byte algorithm = algorithm();
    private boolean handleAsync = handleAsync();
    private Payload payload;

    public abstract byte algorithm();
    public abstract boolean handleAsync();


}
