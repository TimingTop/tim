package com.timing.im.protocol.core;

public class ByteUtil {

    // 分隔符，字节转16进制之间的分隔符
    // 1F2B  => 1F:2B
    public static final char NO_SEPARATOR = 0;
    // 用来转成 16进制
    private final static char[] BIN_TO_HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    // 字节素组 转成 16进制
    public static String byteArray2Hex(byte[] byteArray) {
         return null;
    }

    public static String byteArray2HexString(byte[] byteArray, char sep, int max) {
        if (byteArray != null && byteArray.length != 0) {
            if (max == 0 || max > byteArray.length) {
                max = byteArray.length;
            }

            StringBuilder builder = new StringBuilder(max * (sep == NO_SEPARATOR ? 2 : 3));
            for (int index = 0; index < max; index++) {
                int value = byteArray[index] & 0xFF;

                builder.append(BIN_TO_HEX_ARRAY[value >>> 4]); // 拿到高4位
                builder.append(BIN_TO_HEX_ARRAY[value & 0x0F]); // 拿到低4位
                if (sep != NO_SEPARATOR && index < max - 1) {
                    builder.append(sep);
                }
            }

            return builder.toString();
        }

        return null;
    }
}
