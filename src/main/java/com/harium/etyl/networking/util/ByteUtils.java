package com.harium.etyl.networking.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtils {

    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    public static byte[] shortToBytes(short value) {
        return ByteBuffer.allocate(2).order(BYTE_ORDER).putShort(value).array();
    }

    public static short bytesToShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(BYTE_ORDER).getShort();
    }

    public static byte[] intToBytes(int value) {
        return ByteBuffer.allocate(4).order(BYTE_ORDER).putInt(value).array();
    }

    public static int bytesToInt(byte[] array) {
        return ByteBuffer.wrap(array).order(BYTE_ORDER).getInt();
    }

    public static byte[] floatToBytes(float value) {
        return ByteBuffer.allocate(4).order(BYTE_ORDER).putFloat(value).array();
    }

    public static float bytesToFloat(byte[] array) {
        return ByteBuffer.wrap(array).order(BYTE_ORDER).getFloat();
    }

    public static byte[] doubleToBytes(double value) {
        return ByteBuffer.allocate(8).order(BYTE_ORDER).putDouble(value).array();
    }

    public static double bytesToDouble(byte[] array) {
        return ByteBuffer.wrap(array).order(BYTE_ORDER).getDouble();
    }

}
