package com.timing.demo.io;

import java.io.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class FileChannelTest {

    public static void main(String[] args) throws IOException {


        File file = new File("c://aaaa.txt");

        FileInputStream inputStream = new FileInputStream(file);

        FileChannel channel = inputStream.getChannel();


//        SocketChannel;
//        ServerSocketChannel;
//        DatagramChannel;
        FileOutputStream outputStream = new FileOutputStream(file);
        inputStream.getChannel().transferTo(0, inputStream.available(), outputStream.getChannel());


        FileChannel fileChannel;
    }
}
