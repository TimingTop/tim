package com.timing.im.protocol.packet.codec;

import com.timing.im.protocol.core.RawDataReader;
import com.timing.im.protocol.packet.Packet;

public class PacketDecoder {


    public Packet decode(byte[] msg) throws Exception {
        return null;
    }


    private void parseHeader(RawDataReader reader) {
        // 此协议最低是 是 7byte
        if (reader.available() < 7) {
            return;
        }

        if () {

        }
    }
}
