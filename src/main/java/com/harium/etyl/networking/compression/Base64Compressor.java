package com.harium.etyl.networking.compression;

import com.harium.etyl.networking.util.Base64;

import java.io.IOException;

public class Base64Compressor implements Compressor<String> {

    public String compress(byte[] data) {
        return Base64.encodeBytes(data);
    }

    public byte[] decompress(int length, String data) throws IOException {
        return Base64.decode(data);
    }
}
