package com.harium.etyl.networking.core.helper;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteMessageHelper {

    public static final String EMPTY_STRING = "";
    public static final byte[] EMPTY_BYTES = EMPTY_STRING.getBytes();
    public static final String WHITE_SPACE_STRING = " ";
    public static final byte[] SEPARATOR_BYTES = WHITE_SPACE_STRING.getBytes();

    public static byte[] getPrefix(byte[] message, int length) {
        return Arrays.copyOfRange(message, 0, length);
    }

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

    public static byte[] concatenate(byte[]... chunks) {
        int size = 0;

        for (byte[] chunk : chunks) {
            size += chunk.length;
        }

        byte[] dest = new byte[size];

        int offset = 0;
        for (byte[] chunk : chunks) {
            System.arraycopy(chunk, 0, dest, offset, chunk.length);
            offset += chunk.length;
        }

        return dest;
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

    public static byte[] concatenateMessages(byte[]... chunks) {
        int size = chunks.length * SEPARATOR_BYTES.length - 1;

        for (byte[] chunk : chunks) {
            size += chunk.length;
        }

        // size + separators
        byte[] dest = new byte[size];

        int count = 0;
        int offset = 0;
        for (byte[] chunk : chunks) {
            System.arraycopy(chunk, 0, dest, offset, chunk.length);
            offset += chunk.length;

            if (count < chunks.length - 1) {
                // TODO Iterate over separator bytes if needed
                dest[offset] = SEPARATOR_BYTES[0];
            }

            offset += SEPARATOR_BYTES.length;
            count++;
        }

        return dest;
    }

    public static byte[] subByte(byte[] message, int length) {
        return Arrays.copyOfRange(message, length, message.length);
    }

    public static byte[] wipePrefix(byte[] message, int length) {
        return Arrays.copyOfRange(message, length + SEPARATOR_BYTES.length, message.length);
    }

    public static byte[] wipePrefix(byte[] prefix, byte[] message) {
        return Arrays.copyOfRange(message, prefix.length + SEPARATOR_BYTES.length, message.length);
    }

    public static String wipePrefix(String prefix, String message) {
        return new String(wipePrefix(prefix.getBytes(), message.getBytes()));
    }

    public static boolean equals(String a, String b) {
        return a.equals(b);
    }

    public static boolean equals(byte[] a, byte[] b) {
        return Arrays.equals(a, b);
    }
}
