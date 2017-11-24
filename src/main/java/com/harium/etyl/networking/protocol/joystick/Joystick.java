package com.harium.etyl.networking.protocol.joystick;

/**
 * Class that represents a joystick with digital keys (with two states [pressed and released])
 */
public class Joystick {

    public static final int SIZE = 32; // 32 digital keys (max)

    private int size;
    private boolean[] keys;

    public Joystick() {
        size = SIZE;
        keys = new boolean[size];
    }

    public Joystick(int size) {
        this.size = size;
        keys = new boolean[size];
    }

    public boolean press(int key) {
        boolean state = keys[key];
        if (!state) {
            keys[key] = true;
            return true;
        }
        return false;
    }

    public boolean release(int key) {
        boolean state = keys[key];
        if (state) {
            keys[key] = false;
            return true;
        }
        return false;
    }

    public boolean[] getKeys() {
        return keys;
    }

    public int getSize() {
        return size;
    }

}
