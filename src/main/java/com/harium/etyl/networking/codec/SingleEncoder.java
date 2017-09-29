package com.harium.etyl.networking.codec;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.List;

public class SingleEncoder implements StringCodec {

    protected static final String CHARSET_ASCII = "US-ASCII";
    protected static final String CHARSET_UTF8 = "UTF-8";

    protected final Charset charset = Charset.forName(CHARSET_ASCII);
    protected CharsetDecoder decoder = charset.newDecoder();
    protected CharsetEncoder encoder = charset.newEncoder();

    public SingleEncoder() {
        decoder.onMalformedInput(CodingErrorAction.IGNORE);
    }

    @Override
    public ByteBuffer encode(byte[] message)
            throws CharacterCodingException {

        String text = new String(message);

        ByteBuffer sendBuffer = encoder.encode(CharBuffer.wrap(text));

        encoder.reset();

        return sendBuffer;
    }

    public List<byte[]> decode(ByteBuffer buffer) throws CharacterCodingException {

        List<byte[]> messages = new ArrayList<byte[]>();

        CharBuffer cbuf = decoder.decode(buffer);
        buffer.clear();

        String message = cbuf.toString();
        decoder.reset();

        messages.add(message.getBytes());

        return messages;
    }

}
