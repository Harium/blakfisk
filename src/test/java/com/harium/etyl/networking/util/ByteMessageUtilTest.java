package com.harium.etyl.networking.util;

import org.junit.Assert;
import org.junit.Test;

public class ByteMessageUtilTest {

    public static final byte SEP = ByteMessageUtils.SEPARATOR_BYTES[0];

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
    public void testGetBytePrefixFromLength() {
        byte[] original = new byte[]{1, 2, 3};
        byte[] result = ByteMessageUtils.getPrefix(original, 2);

        Assert.assertArrayEquals(new byte[]{1, 2}, result);
    }

    @Test
    public void testConcanateMultipleArrays() {
        byte[] a = "a".getBytes();
        byte[] b = "b".getBytes();
        byte[] c = "c".getBytes();

        byte[] result = ByteMessageUtils.concatenate(a, b, c);

        Assert.assertArrayEquals(new byte[]{a[0], b[0], c[0]}, result);
    }

    @Test
    public void testConcanateMultipleMessages() {
        byte[] a = "a".getBytes();
        byte[] b = "b".getBytes();
        byte[] c = "c".getBytes();

        byte[] result = ByteMessageUtils.concatenateMessages(a, b, c);

        Assert.assertArrayEquals(new byte[]{a[0], SEP, b[0], SEP, c[0]}, result);
    }

    @Test
    public void testWipePrefix() {
        String message = "Hello my friend";
        byte[] prefix = ByteMessageUtils.getPrefix(message.getBytes());
        byte[] result = ByteMessageUtils.wipePrefix(prefix, message.getBytes());

        Assert.assertArrayEquals("my friend".getBytes(), result);
    }

    @Test
    public void testWipePrefixFromLength() {
        byte[] original = new byte[]{1, 2, SEP, 3};
        byte[] result = ByteMessageUtils.wipePrefix(original, 2);

        Assert.assertArrayEquals(new byte[]{3}, result);
    }

    @Test
    public void testSubByte() {
        byte[] message = new byte[]{49, 32, 35, 32, 99};
        byte[] slice = ByteMessageUtils.subByte(message, 4);

        Assert.assertArrayEquals(new byte[]{99}, slice);
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

    @Test
    public void testShortToBytes() {
        byte[] array = ByteMessageUtils.shortToBytes((short)123);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(123, array[0]);

        array = ByteMessageUtils.shortToBytes(Short.MAX_VALUE);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(-1, array[0]);
        Assert.assertEquals(127, array[1]);

        array = ByteMessageUtils.shortToBytes(Short.MIN_VALUE);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(0, array[0]);
        Assert.assertEquals(-128, array[1]);

        array = ByteMessageUtils.shortToBytes((short)0);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(0, array[0]);
        Assert.assertEquals(0, array[1]);
    }

    @Test
    public void testBytesToShort() {
        byte[] array = new byte[]{1,2};
        short s = ByteMessageUtils.bytesToShort(array);
        Assert.assertEquals(513, s);

        s = ByteMessageUtils.bytesToShort(new byte[]{-128,-128});
        Assert.assertEquals(-32640, s);

        s = ByteMessageUtils.bytesToShort(new byte[]{127,127});
        Assert.assertEquals(32639, s);
    }

}
