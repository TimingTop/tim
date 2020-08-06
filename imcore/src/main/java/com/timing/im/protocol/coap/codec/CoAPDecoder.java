package com.timing.im.protocol.coap.codec;

import com.timing.im.protocol.coap.CoAP;
import com.timing.im.protocol.coap.MessageHeader;
import com.timing.im.protocol.coap.Token;
import com.timing.im.protocol.core.RawDataReader;
import com.timing.im.protocol.coap.Message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class CoAPDecoder {

    public Message decode(final byte[] msg) {

        RawDataReader reader = new RawDataReader(new ByteArrayInputStream(msg));




        return null;
    }

    public final Message decode0(final byte[] msg) {

        // 把原始字符数组 封装一层
        RawDataReader reader = new RawDataReader(new ByteArrayInputStream(msg));
        // 先把头 解析出来
        MessageHeader header = parseHeader(reader);

        // 然后解析 option & body


        return null;
    }

    protected MessageHeader parseHeader(RawDataReader reader) {
        int version = reader.read(CoAP.MessageFormat.VERSION_BITS);
        int type = reader.read(CoAP.MessageFormat.TYPE_BITS);
        int tokenLength = reader.read(CoAP.MessageFormat.TOKEN_LENGTH_BITS);
        int code = reader.read(CoAP.MessageFormat.CODE_BITS);
        int mid = reader.read(CoAP.MessageFormat.MESSAGE_ID_BITS);

        Token token = Token.fromProvider(reader.readBytes(tokenLength));

        return new MessageHeader(version, CoAP.Type.valueOf(type), token, code, mid, 0);
    }



    private void parseOptionsAndPayload(RawDataReader reader, Message message) {
        int currentOptionNumber = 0;
        byte nextByte = 0;

        while (reader.bytesAvailable()) {
            nextByte = reader.readNextByte();
            // 0xFF payload 的开始标识符，options 是保留了给 payload 用的
            if (nextByte != CoAP.MessageFormat.PAYLOAD_MARKER) {
                // todo
                // 获取改字节的 高四位 ， option delta
                // 低 4 位 ， option length
                int optionDeltaNibble = (0xF0 & nextByte) >> 4;
            }
        }

    }

    private void testIoStream() {
//        DataInputStream dataInputStream = new DataInputStream()
        System.out.println("aaaa");

    }
}
