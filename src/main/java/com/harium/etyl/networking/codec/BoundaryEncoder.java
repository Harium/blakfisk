package com.harium.etyl.networking.codec;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

public class BoundaryEncoder implements StringCodec {

    public static final int INT_SIZE = 4;

    public static final int BUFFER_SIZE = 8192;

    protected static final String CHARSET_ASCII = "US-ASCII";

    public static final String EMPTY_MESSAGE = "";

    protected final Charset charset = Charset.forName(CHARSET_ASCII);
    protected CharsetDecoder decoder = charset.newDecoder();
    protected CharsetEncoder encoder = charset.newEncoder();

    @Override
    public ByteBuffer encode(byte[] message) throws CharacterCodingException {

        String text = new String(message);

        //String rawMessage = message;
        ByteBuffer encodedBuffer = encoder.encode(CharBuffer.wrap(text));

        ByteBuffer sendBuffer = ByteBuffer.allocate(BUFFER_SIZE + INT_SIZE);
        sendBuffer.putInt(text.length());
        sendBuffer.put(encodedBuffer);
        sendBuffer.flip();

        encoder.reset();

        return sendBuffer;
    }

    public List<byte[]> decode(ByteBuffer buffer) throws CharacterCodingException {

        List<byte[]> messages = new ArrayList<byte[]>();

        int initialLimit = buffer.limit();

        int currentPosition = 0;

        String message = "";

        do {
            message = readMessage(buffer, currentPosition);

            if (message.isEmpty())
                break;

            messages.add(message.getBytes());

            currentPosition += message.length() + INT_SIZE;

            if (currentPosition >= initialLimit) {
                break;
            }

            buffer.limit(buffer.capacity());
            buffer.position(currentPosition);
            buffer.limit(initialLimit);

        } while (!message.isEmpty());

        buffer.clear();

        return messages;
    }

    private String readMessage(ByteBuffer buffer, int lastLimit) throws BufferUnderflowException, CharacterCodingException {

        try {
            int messageSize = buffer.getInt();

            int limit = messageSize + lastLimit;

            if (limit > 0) {
                try {
                    buffer.limit(limit + INT_SIZE);

                    CharBuffer cbuf = decoder.decode(buffer);
                    decoder.reset();
                    return cbuf.toString();
                } catch (IllegalArgumentException e) {
                    return EMPTY_MESSAGE;
                }
            }
        } catch (BufferUnderflowException e) {
            return EMPTY_MESSAGE;
        }

        return EMPTY_MESSAGE;
    }
}
