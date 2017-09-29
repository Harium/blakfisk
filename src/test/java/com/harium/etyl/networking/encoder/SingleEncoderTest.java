package com.harium.etyl.networking.encoder;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.List;

import com.harium.etyl.networking.codec.SingleEncoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SingleEncoderTest {

	private SingleEncoder singleEncoder;

	@Before
	public void setUp() {
		singleEncoder = new SingleEncoder();
	}
	
	@Test
	public void testEncodeSingleMessage() throws CharacterCodingException {
		String message = "Hello Mars!";
		
		ByteBuffer buffer = singleEncoder.encode(message.getBytes());
		
		Assert.assertEquals(message.length(), buffer.capacity());
	}
	
	@Test
	public void testDecodeSingleMessage() throws CharacterCodingException {
		
		String message = "Hello Mars!";
		
		ByteBuffer buffer = singleEncoder.encode(message.getBytes());
				
		List<byte[]> decodedMessage = singleEncoder.decode(buffer);
		
		Assert.assertEquals(1, decodedMessage.size());
		Assert.assertEquals(message, new String(decodedMessage.get(0)));
	}
	
}
