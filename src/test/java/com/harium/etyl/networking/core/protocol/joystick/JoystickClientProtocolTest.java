package com.harium.etyl.networking.core.protocol.joystick;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JoystickClientProtocolTest {

    JoystickClientProtocol protocol;
    Joystick joystick;

    @Before
    public void setUp() {
        protocol = new JoystickClientProtocol("", null);
        joystick = protocol.joystick;
    }

    @Test
    public void testJoystickInitialState() {
        for (int i = 0; i < Joystick.SIZE; i++) {
            Assert.assertFalse(protocol.joystick.getKeys()[i]);
        }
    }

    @Test
    public void testJoystickState() {
        int index = 0;
        protocol.press(index);
        Assert.assertTrue(protocol.joystick.getKeys()[index]);
        protocol.release(index);
        Assert.assertFalse(protocol.joystick.getKeys()[index]);
    }

    @Test
    public void testMappingKeys() {
        int key = 123;
        int index = 0;

        protocol.mapKey(key, index);
        Assert.assertTrue(protocol.keyMap.containsKey(key));
        Assert.assertEquals(index, protocol.keyIndex(key));
    }

}
