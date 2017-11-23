package com.harium.etyl.networking.util;

import org.junit.Assert;
import org.junit.Test;

public class ByteMessageUtilTest {

    @Test
    public void testConcatenate() {
        String message = "my friend";
        String prefix = "Hello";

        String result = new String(ByteMessageUtils.concatenateMessage(prefix.getBytes(), message.getBytes()));

        Assert.assertEquals("Hello my friend", result);
    }

    @Test
    public void testGetPrefix() {
        String message = "Hello my friend";
        String prefix = ByteMessageUtils.getPrefix(message);

        Assert.assertEquals("Hello", prefix);
    }

    @Test
    public void testGetBytePrefix() {
        String message = "Hello my friend";
        byte[] prefix = ByteMessageUtils.getPrefix(message.getBytes());

        Assert.assertArrayEquals("Hello".getBytes(), prefix);
    }

    @Test
    public void testWipePrefix() {
        String message = "Hello my friend";
        byte[] prefix = ByteMessageUtils.getPrefix(message.getBytes());

        byte[] wipe = ByteMessageUtils.wipePrefix(prefix, message.getBytes());
        System.out.println(new String(wipe));

        Assert.assertArrayEquals("my friend".getBytes(), wipe);
    }

    @Test
    public void testIntToByte() {
        byte[] array = ByteMessageUtils.intToBytes(123);
        Assert.assertEquals(4, array.length);
        Assert.assertEquals(123, array[0]);
    }

    @Test
    public void testBigIntToByte() {
        byte[] array = ByteMessageUtils.intToBytes(Integer.MAX_VALUE);

        Assert.assertEquals(4, array.length);
        Assert.assertEquals(-1, array[0]);
        Assert.assertEquals(-1, array[1]);
        Assert.assertEquals(-1, array[2]);
        Assert.assertEquals(Byte.MAX_VALUE, array[3]);
    }

}
