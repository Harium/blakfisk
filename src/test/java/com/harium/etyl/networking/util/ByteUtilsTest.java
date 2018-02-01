package com.harium.etyl.networking.util;

import org.junit.Assert;
import org.junit.Test;

public class ByteUtilsTest {

    @Test
    public void testIntToByte() {
        byte[] array = ByteUtils.intToBytes(123);
        Assert.assertEquals(4, array.length);
        Assert.assertEquals(123, array[0]);
    }

    @Test
    public void testBigIntToByte() {
        byte[] array = ByteUtils.intToBytes(Integer.MAX_VALUE);

        Assert.assertEquals(4, array.length);
        Assert.assertEquals(-1, array[0]);
        Assert.assertEquals(-1, array[1]);
        Assert.assertEquals(-1, array[2]);
        Assert.assertEquals(Byte.MAX_VALUE, array[3]);
    }

    @Test
    public void testShortToBytes() {
        byte[] array = ByteUtils.shortToBytes((short)123);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(123, array[0]);

        array = ByteUtils.shortToBytes(Short.MAX_VALUE);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(-1, array[0]);
        Assert.assertEquals(127, array[1]);

        array = ByteUtils.shortToBytes(Short.MIN_VALUE);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(0, array[0]);
        Assert.assertEquals(-128, array[1]);

        array = ByteUtils.shortToBytes((short)0);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(0, array[0]);
        Assert.assertEquals(0, array[1]);
    }

    @Test
    public void testBytesToShort() {
        byte[] array = new byte[]{1,2};
        short s = ByteUtils.bytesToShort(array);
        Assert.assertEquals(513, s);

        s = ByteUtils.bytesToShort(new byte[]{-128,-128});
        Assert.assertEquals(-32640, s);

        s = ByteUtils.bytesToShort(new byte[]{127,127});
        Assert.assertEquals(32639, s);
    }

}
