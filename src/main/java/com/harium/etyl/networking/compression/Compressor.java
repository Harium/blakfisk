package com.harium.etyl.networking.compression;

import java.io.IOException;

public interface Compressor<T> {
    T compress(byte[] data) throws IOException;

    byte[] decompress(int originalLength, T data) throws IOException;
}
