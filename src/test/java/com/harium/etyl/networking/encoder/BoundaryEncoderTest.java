package com.harium.etyl.networking.encoder;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.List;

import com.harium.etyl.networking.codec.BoundaryEncoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BoundaryEncoderTest {

	private BoundaryEncoder boundaryEncoder;

	@Before
	public void setUp() {
		boundaryEncoder = new BoundaryEncoder();
	}
		
	@Test
	public void testEncodeBoundMessage() throws CharacterCodingException {
		
		String message = "Hello Mars!";
		
		ByteBuffer buffer = boundaryEncoder.encode(message.getBytes());
		
		Assert.assertNotEquals(0, buffer.limit());
				
		List<byte[]> decodedMessages = boundaryEncoder.decode(buffer);
		
		Assert.assertEquals(1, decodedMessages.size());
		Assert.assertEquals("Hello Mars!", new String(decodedMessages.get(0)));
	}
	
	@Test
	public void testDecodeBoundaryMessages() throws CharacterCodingException {
		
		String firstMessage = "Hello Mars!";
		String secondMessage = "Good Morning Sun!";
		String thirdMessage = "Hello Sunshine!";
		
		ByteBuffer buffer = ByteBuffer.allocate(BoundaryEncoder.BUFFER_SIZE);
		
		int count = 0;
		
		byte[] raw = firstMessage.getBytes();
		count += raw.length+BoundaryEncoder.INT_SIZE;
		buffer.putInt(raw.length);
		buffer.put(raw);
		
		raw = secondMessage.getBytes();
		count += raw.length+BoundaryEncoder.INT_SIZE;
		buffer.putInt(raw.length);
		buffer.put(raw);
		
		raw = thirdMessage.getBytes();
		count += raw.length+BoundaryEncoder.INT_SIZE;
		buffer.putInt(raw.length);
		buffer.put(raw);
		
		//This flip is changes buffer to read mode
		buffer.flip();
		
		List<byte[]> decodedMessages = boundaryEncoder.decode(buffer);
		
		Assert.assertEquals(firstMessage, new String(decodedMessages.get(0)));
		Assert.assertEquals(secondMessage, new String(decodedMessages.get(1)));
		Assert.assertEquals(thirdMessage, new String(decodedMessages.get(2)));
	}
	
}
