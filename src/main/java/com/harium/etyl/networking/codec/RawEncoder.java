package com.harium.etyl.networking.codec;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.List;

public class RawEncoder implements StringCodec {

    @Override
    public ByteBuffer encode(byte[] message)
            throws CharacterCodingException {
        ByteBuffer sendBuffer = ByteBuffer.wrap(message);

        return sendBuffer;
    }

    public List<byte[]> decode(ByteBuffer buffer) throws CharacterCodingException {
        byte[] bytes = new byte[buffer.capacity()];
        buffer.limit(buffer.capacity());
        buffer.get(bytes, 0, bytes.length);

        List<byte[]> messages = new ArrayList<byte[]>(1);
        messages.add(bytes);

        return messages;
    }

}
