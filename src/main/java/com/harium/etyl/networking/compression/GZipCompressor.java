package com.harium.etyl.networking.compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipCompressor implements Compressor<byte[]> {

    @Override
    public byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(data);
        gos.close();
        byte[] compressed = os.toByteArray();
        os.close();

        return compressed;
    }

    @Override
    public byte[] decompress(int originalLength, byte[] data)
            throws IOException {
        GZIPInputStream os = new GZIPInputStream(new ByteArrayInputStream(data));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int value = 0; value != -1; ) {
            value = os.read();
            if (value != -1) {
                baos.write(value);
            }
        }
        os.close();
        baos.close();

        return baos.toByteArray();
    }

}
