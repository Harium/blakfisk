package com.harium.etyl.networking.protocol.joystick;

import com.harium.etyl.networking.model.Peer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
