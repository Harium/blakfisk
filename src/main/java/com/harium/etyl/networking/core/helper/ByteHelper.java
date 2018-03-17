package com.harium.etyl.networking.core.helper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteHelper {

    private static final int SHORT_SIZE = 2;
    private static final int INT_SIZE = 4;
    private static final int FLOAT_SIZE = 4;
    private static final int DOUBLE_SIZE = 8;

    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    public static byte[] shortToBytes(short value) {
        return ByteBuffer.allocate(SHORT_SIZE).order(BYTE_ORDER).putShort(value).array();
    }

    public static byte[] shortToBytes(short value, byte[] out, int offset) {
        return ByteBuffer.wrap(out, offset, SHORT_SIZE).order(BYTE_ORDER).putShort(value).array();
    }

    public static short bytesToShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(BYTE_ORDER).getShort();
    }

    public static short bytesToShort(byte[] bytes, int offset) {
        return ByteBuffer.wrap(bytes, offset, SHORT_SIZE).order(BYTE_ORDER).getShort();
    }

    public static byte[] intToBytes(int value) {
        return ByteBuffer.allocate(INT_SIZE).order(BYTE_ORDER).putInt(value).array();
    }

    public static byte[] intToBytes(int value, byte[] out, int offset) {
        return ByteBuffer.wrap(out, offset, INT_SIZE).order(BYTE_ORDER).putInt(value).array();
    }

    public static int bytesToInt(byte[] array) {
        return ByteBuffer.wrap(array).order(BYTE_ORDER).getInt();
    }

    public static int bytesToInt(byte[] array, int offset) {
        return ByteBuffer.wrap(array, offset, INT_SIZE).order(BYTE_ORDER).getInt();
    }

    public static byte[] floatToBytes(float value) {
        return ByteBuffer.allocate(FLOAT_SIZE).order(BYTE_ORDER).putFloat(value).array();
    }

    public static byte[] floatToBytes(float value, byte[] out, int offset) {
        return ByteBuffer.wrap(out, offset, FLOAT_SIZE).order(BYTE_ORDER).putFloat(value).array();
    }

    public static float bytesToFloat(byte[] array) {
        return ByteBuffer.wrap(array).order(BYTE_ORDER).getFloat();
    }

    public static float bytesToFloat(byte[] array, int offset) {
        return ByteBuffer.wrap(array, offset, FLOAT_SIZE).order(BYTE_ORDER).getFloat();
    }

    public static byte[] doubleToBytes(double value) {
        return ByteBuffer.allocate(DOUBLE_SIZE).order(BYTE_ORDER).putDouble(value).array();
    }

    public static byte[] doubleToBytes(double value, byte[] out, int offset) {
        return ByteBuffer.wrap(out, offset, DOUBLE_SIZE).order(BYTE_ORDER).putDouble(value).array();
    }

    public static double bytesToDouble(byte[] array) {
        return ByteBuffer.wrap(array).order(BYTE_ORDER).getDouble();
    }

    public static double bytesToDouble(byte[] array, int offset) {
        return ByteBuffer.wrap(array, offset, DOUBLE_SIZE).order(BYTE_ORDER).getDouble();
    }

}
