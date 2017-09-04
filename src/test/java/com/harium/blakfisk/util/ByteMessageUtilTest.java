package com.harium.blakfisk.util;

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
	
}
