package com.harium.etyl.networking.core.protocol.joystick;

public interface JoystickListener {
    void onPressed(int joystickId, int key);
    void onReleased(int joystickId, int key);
}
