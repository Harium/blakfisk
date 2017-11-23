package com.harium.etyl.networking.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteMessageUtils {

    public static final String EMPTY_STRING = "";
    public static final byte[] EMPTY_BYTES = EMPTY_STRING.getBytes();
    public static final String WHITE_SPACE_STRING = " ";
    public static final byte[] SEPARATOR_BYTES = WHITE_SPACE_STRING.getBytes();
    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    public static byte[] getPrefix(byte[] message) {

        byte[] prefix;

        int i = 0;
        for (; i < message.length; i++) {
            if (message[i] == SEPARATOR_BYTES[0]) {
                break;
            }
        }

        prefix = new byte[i];

        System.arraycopy(message, 0, prefix, 0, i);
        return prefix;
    }

    public static String getPrefix(String message) {
        if (message.isEmpty() || !message.contains(WHITE_SPACE_STRING)) {
            return EMPTY_STRING;
        }

        String prefix = message.substring(0, message.indexOf(WHITE_SPACE_STRING));
        return prefix;
    }

    public static byte[] concatenate(byte[] a, byte[] b) {
        byte[] destination = new byte[a.length + b.length];

        System.arraycopy(a, 0, destination, 0, a.length);
        System.arraycopy(b, 0, destination, a.length, b.length);

        return destination;
    }

    public static byte[] concatenate(byte[]... array) {
        int len = 0;
        for (int i = 0; i < array.length; i++) {
            len += array[i].length;
        }
        byte[] destination = new byte[len];

        int from = 0;
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(array[i], 0, destination, from, array[i].length);
            from += array[i].length;
        }

        return destination;
    }

    public static byte[] concatenateMessage(byte[] prefix, byte[] message) {
        byte[] destination = new byte[prefix.length + SEPARATOR_BYTES.length + message.length];

        System.arraycopy(prefix, 0, destination, 0, prefix.length);
        // 1 byte separator
        destination[prefix.length] = SEPARATOR_BYTES[0];
        //System.arraycopy(SEPARATOR_BYTES, 0, destination, prefix.length, SEPARATOR_BYTES.length);
        System.arraycopy(message, 0, destination, prefix.length + SEPARATOR_BYTES.length, message.length);

        return destination;
    }

    public static byte[] wipePrefix(byte[] prefix, byte[] message) {
        int index = prefix.length + SEPARATOR_BYTES.length;
        int size = message.length - index;
        if (size <= 0) {
            return new byte[0];
        }
        byte[] destination = new byte[size];

        System.arraycopy(message, index, destination, 0, destination.length);

        return destination;
    }

    public static String wipePrefix(String prefix, String message) {
        return new String(wipePrefix(prefix.getBytes(), message.getBytes()));
    }

    public static byte[] intToBytes(int value) {
        return ByteBuffer.allocate(4).order(BYTE_ORDER).putInt(value).array();
    }

    public static int bytesToInt(byte[] array) {
        return ByteBuffer.wrap(array).order(BYTE_ORDER).getInt();
    }
}
