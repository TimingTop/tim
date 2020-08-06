package com.test;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class InputStreamTest {
    public static void main(String[] args) throws IOException {

        ByteArrayInputStream bais = null;
        StringBuilder sb = new StringBuilder();





        byte[] b = "Today is montday!, how nice you".getBytes();
        bais = new ByteArrayInputStream(b, 2, 10);

        byte[] temp = new byte[20];
        // 读下一个字节
        bais.read();
        // 将n个字节读入数组
        bais.read(temp, 0, 1);
        // 返回可读的字节数
        bais.available();
        // 跳过几个字节
        bais.skip(2L);






        bais.close();



    }
    @Test
    public void testStringbuild() {
        StringBuilder sb = new StringBuilder(2);
        sb.append("a");
        sb.append("b");
        sb.append("c");
        sb.append("d");
        sb.append("e");
        System.out.println(sb.toString());
    }
}
