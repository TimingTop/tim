package com.timing.im.protocol.coap.codec;

import com.timing.im.protocol.coap.CoAP;
import com.timing.im.protocol.coap.Message;
import com.timing.im.protocol.coap.MessageHeader;
import com.timing.im.protocol.coap.Token;
import com.timing.im.protocol.core.RawDataReader;

// tcp 的 coap 协议解码
// https://tools.ietf.org/id/draft-ietf-core-coap-tcp-tls-11.html#:~:text=Conceptually%2C%20CoAP%20over%20TCP%20replaces,the%20UDP%2FDTLS%20datagram%20layer.
// 参照协议解析
public class TcpDecoder extends Decoder {

    @Override
    protected MessageHeader parseHeader(RawDataReader reader) {
        if (!reader.bytesAvailable(1)) {
            throw new IllegalArgumentException("The reader must avaliable than 1");
        }
        int len = reader.read(CoAP.MessageFormat.LENGTH_NIBBLE_BITS);
        int tokenLength = reader.read(CoAP.MessageFormat.TOKEN_LENGTH_BITS);
        int lengthSize = 0;
        if (len == 13) {
            lengthSize = 1;
        } else if (len == 14) {
            lengthSize = 2;
        } else if (len == 15) {
            lengthSize = 4;
        }
        int size = lengthSize // 长度扩展字段 byte 数
                   + 1  // code 1byte
                   + tokenLength; // token bytes

        if (!reader.bytesAvailable(size)) {
            throw new IllegalArgumentException("not enough");
        }

        reader.readBytes(lengthSize);
        int code = reader.read(CoAP.MessageFormat.CODE_BITS);
        Token token = Token.fromProvider(reader.readBytes(tokenLength));

        return new MessageHeader(CoAP.VERSION, CoAP.Type.CON,
                token, code, Message.NONE, 0);

    }
}
