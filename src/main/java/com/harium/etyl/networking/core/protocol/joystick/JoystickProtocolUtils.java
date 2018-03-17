package com.harium.etyl.networking.core.protocol.joystick;

public class JoystickProtocolUtils {

    protected static final int UNDEFINED_VALUE = -1;

    public static int booleanToInt(boolean[] array) {
        int result = 0;
        int count = 1;
        for (boolean bit : array) {
            if (bit) {
                result += count;
            }
            count *= 2;
        }
        return result;
    }

    public static boolean[] intToBoolean(int value, int size) {
        boolean[] flags = new boolean[size];
        for (int i = 0; i < size; ++i) {
            flags[i] = (value & (1 << i)) != 0;
        }
        return flags;
    }
}
