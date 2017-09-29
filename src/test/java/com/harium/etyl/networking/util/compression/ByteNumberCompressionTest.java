package com.harium.etyl.networking.util.compression;

import org.junit.Assert;
import org.junit.Test;

public class ByteNumberCompressionTest {

	@Test
	public void testEncodeTwoBytesNumber() {
	
		int number = 136;
		byte[] b = ByteNumberCompression.encode(number);

		Assert.assertEquals(2, b.length);
		Assert.assertEquals(8, b[0]);
		Assert.assertEquals(-128, b[1]);
		
		Assert.assertEquals(number, ByteNumberCompression.decode(b));
	}
	
	@Test
	public void testEncodeAnotherTwoBytesNumber() {
	
		int number = 57509;
		byte[] b = ByteNumberCompression.encode(number);

		Assert.assertEquals(2, b.length);
		Assert.assertEquals(37, b[0]);
		Assert.assertEquals(96, b[1]);
		
		Assert.assertEquals(number, ByteNumberCompression.decode(b));
	}
	
	@Test
	public void testEncodeThreeBytesNumber() {
		
		int number = 16777215;//Overflow at 16777216
		byte[] b = ByteNumberCompression.encode(number, 3);

		Assert.assertEquals(3, b.length);
		Assert.assertEquals(127, b[0]);
		Assert.assertEquals(127, b[1]);
		Assert.assertEquals(127, b[2]);
				
		Assert.assertEquals(number, ByteNumberCompression.decode(b));
	}
	
	
	
}
