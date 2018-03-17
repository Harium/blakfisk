package com.harium.etyl.networking.core.helper;

import org.junit.Assert;
import org.junit.Test;

public class ByteHelperTest {

    @Test
    public void testIntToByte() {
        byte[] array = ByteHelper.intToBytes(123);
        Assert.assertEquals(4, array.length);
        Assert.assertEquals(123, array[0]);
    }

    @Test
    public void testBigIntToByte() {
        byte[] array = ByteHelper.intToBytes(Integer.MAX_VALUE);

        Assert.assertEquals(4, array.length);
        Assert.assertEquals(-1, array[0]);
        Assert.assertEquals(-1, array[1]);
        Assert.assertEquals(-1, array[2]);
        Assert.assertEquals(Byte.MAX_VALUE, array[3]);
    }

    @Test
    public void testShortToBytes() {
        byte[] array = ByteHelper.shortToBytes((short)123);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(123, array[0]);

        array = ByteHelper.shortToBytes(Short.MAX_VALUE);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(-1, array[0]);
        Assert.assertEquals(127, array[1]);

        array = ByteHelper.shortToBytes(Short.MIN_VALUE);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(0, array[0]);
        Assert.assertEquals(-128, array[1]);

        array = ByteHelper.shortToBytes((short)0);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(0, array[0]);
        Assert.assertEquals(0, array[1]);
    }

    @Test
    public void testBytesToShort() {
        byte[] array = new byte[]{1,2};
        short s = ByteHelper.bytesToShort(array);
        Assert.assertEquals(513, s);

        s = ByteHelper.bytesToShort(new byte[]{-128,-128});
        Assert.assertEquals(-32640, s);

        s = ByteHelper.bytesToShort(new byte[]{127,127});
        Assert.assertEquals(32639, s);
    }

}
