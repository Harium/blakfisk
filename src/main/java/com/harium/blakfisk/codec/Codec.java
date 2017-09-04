package com.harium.blakfisk.codec;

import java.util.List;

public interface Codec<B> {
    B encode(byte[] message) throws Exception;

    List<byte[]> decode(B buffer) throws Exception;
}
