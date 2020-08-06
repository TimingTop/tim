package com.test;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class DatagramNIOChannel {

    @Test
    public void test1() throws Exception {
        DatagramChannel channel = DatagramChannel.open();

        channel.bind(new InetSocketAddress(8989));

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();

        channel.receive(buffer);

        // write the data

        buffer.clear();

        buffer.put("aaaaa".getBytes());

        buffer.flip();

        channel.send(buffer, new InetSocketAddress(8080));



    }
}
