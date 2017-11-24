package com.harium.etyl.networking.protocol.joystick;

import org.junit.Assert;
import org.junit.Test;

public class JoystickProtocolUtilsTest {

    @Test
    public void testBooleanToInt() {
        boolean[] array;

        array = new boolean[]{false};
        Assert.assertEquals(0, JoystickProtocolUtils.booleanToInt(array));

        array = new boolean[]{true};
        Assert.assertEquals(1, JoystickProtocolUtils.booleanToInt(array));

        array = new boolean[]{true, true};
        Assert.assertEquals(3, JoystickProtocolUtils.booleanToInt(array));

        array = new boolean[]{false, true, true};
        Assert.assertEquals(6, JoystickProtocolUtils.booleanToInt(array));

        array = new boolean[]{true, true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true, true,
                true, true, true, true, true, true};
        Assert.assertEquals(Integer.MAX_VALUE, JoystickProtocolUtils.booleanToInt(array));
    }

    @Test
    public void intToBoolean() {
        int size = Joystick.SIZE;

        boolean[] none = new boolean[]{false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false};
        Assert.assertArrayEquals(none, JoystickProtocolUtils.intToBoolean(0, size));

        boolean[] all = new boolean[]{true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true, false};
        Assert.assertArrayEquals(all, JoystickProtocolUtils.intToBoolean(Integer.MAX_VALUE, size));
    }

}
