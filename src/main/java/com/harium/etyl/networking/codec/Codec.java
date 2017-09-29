package com.harium.etyl.networking.codec;

import java.util.List;

public interface Codec<B> {
    B encode(byte[] message) throws Exception;

    List<byte[]> decode(B buffer) throws Exception;
}
