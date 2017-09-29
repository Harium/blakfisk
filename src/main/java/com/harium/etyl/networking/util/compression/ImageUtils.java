package com.harium.etyl.networking.util.compression;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ImageUtils {

	/**
	 * Based on code found at: https://github.com/arfan/java-yuv/blob/master/WriteYUV.java
	 * @param width
	 * @param height
	 * @param argb
	 * @return
	 */
	public static byte[] convertRGBtoYUV420(int width, int height, int[] argb) {
        final int frameSize = width * height;
        final int chromasize = frameSize / 4;
       
        int yIndex = 0;
        int uIndex = frameSize;
        int vIndex = frameSize + chromasize;
        byte[] yuv = new byte[width*height*3/2];
       
        int a, R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
            	//a = (aRGB[index] & 0xff000000) >> 24; //not using it right now
                R = (argb[index] & 0xff0000) >> 16;
                G = (argb[index] & 0xff00) >> 8;
                B = (argb[index] & 0xff) >> 0;

                Y = (int)(R * .299000 + G * .587000 + B * 0.114000);
                U = (int)(R * -.168736 + G * -.331264 + B * 0.500000 + 128);
                V = (int)(R * .500000 + G * -.418688 + B * -0.081312 + 128);
                
                yuv[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
               
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv[uIndex++] = (byte)((U < 0) ? 0 : ((U > 255) ? 255 : U));
                    yuv[vIndex++] = (byte)((V < 0) ? 0 : ((V > 255) ? 255 : V));
                }

                index++;
            }
        }
        return yuv;
    }
	
	public static byte[] convertRGBByteArratyToYUV420(int width, int height, byte[] rgb) {
        final int frameSize = width * height;
        final int chromasize = frameSize / 4;
       
        int yIndex = 0;
        int uIndex = frameSize;
        int vIndex = frameSize + chromasize;
        byte [] yuv = new byte[width*height*3/2];
       
        int R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                R = rgb[width * 3 * j + i*3 + 0];
                G = rgb[width * 3 * j + i*3 + 1];
                B = rgb[width * 3 * j + i*3 + 2];

                Y = (int)(R * .299000 + G * .587000 + B * 0.114000);
                U = (int)(R * -.168736 + G * -.331264 + B * 0.500000 + 128);
                V = (int)(R * .500000 + G * -.418688 + B * -0.081312 + 128);

                yuv[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
               
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv[uIndex++] = (byte)((U < 0) ? 0 : ((U > 255) ? 255 : U));
                    yuv[vIndex++] = (byte)((V < 0) ? 0 : ((V > 255) ? 255 : V));
                }

                index++;
            }
        }
        return yuv;
    }
	
	/**
	 * Based on code found at: https://github.com/arfan/java-yuv/blob/master/ReadYUV.java
	 * @param w
	 * @param h
	 * @param data
	 * @return
	 */
	public static int[] convertYUVtoARGB(int w, int h, byte[] data) {
		int numPixels = w * h;
		IntBuffer intBuffer = IntBuffer.allocate(numPixels);
        // If you're reusing a buffer, next line imperative to refill from the start,
        // if not good practice
        intBuffer.position(0);

        // Get each pixel, one at a time
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
            	int arraySize = h * w;
        		int Y = unsignedByteToInt(data[y * w + x]);
        		int U = unsignedByteToInt(data[(y/2) * (w/2) + x/2 + arraySize]);
        		int V = unsignedByteToInt(data[(y/2) * (w/2) + x/2 + arraySize + arraySize/4]);
                // Do the YUV -> RGB conversion
                
                int R = (int)(Y + 1.4075 * (V - 128));
                int G = (int)(Y - 0.3455 * (U - 128) - (0.7169 * (V - 128)));
                int B =(int) (Y + 1.7790 * (U - 128));
                int alpha = 1; //unless transparent

                // Clip rgb values to 0-255
                R = R < 0 ? 0 : R > 255 ? 255 : R;
                G = G < 0 ? 0 : G > 255 ? 255 : G;
                B = B < 0 ? 0 : B > 255 ? 255 : B;

                // Put that pixel in the buffer
                intBuffer.put(alpha * 16777216 + R * 65536 + G * 256 + B);
            }
        }

        // Get buffer ready to be read
        intBuffer.flip();
        return intBuffer.array();
	}
	
	public static byte[] convertYUVtoRGBByteArray(int w, int h, byte[] data) {
		int numPixels = w * h * 3; //3 RGB channels
		ByteBuffer buffer = ByteBuffer.allocate(numPixels);
		// If you're reusing a buffer, next line imperative to refill from the start,
		// if not good practice
		buffer.position(0);

		// Get each pixel, one at a time
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int arraySize = h * w;
        		int Y = unsignedByteToInt(data[y * w + x]);
        		int U = unsignedByteToInt(data[(y/2) * (w/2) + x/2 + arraySize]);
        		int V = unsignedByteToInt(data[(y/2) * (w/2) + x/2 + arraySize + arraySize/4]);
                // Do the YUV -> RGB conversion
                
                int R = (int)(Y + 1.4075 * (V - 128));
                int G = (int)(Y - 0.3455 * (U - 128) - (0.7169 * (V - 128)));
                int B =(int) (Y + 1.7790 * (U - 128));

				// Clip rgb values to 0-255
				R = R < 0 ? 0 : R > 255 ? 255 : R;
				G = G < 0 ? 0 : G > 255 ? 255 : G;
				B = B < 0 ? 0 : B > 255 ? 255 : B;

				// Put rgb in the buffer
				buffer.put((byte)R);
				buffer.put((byte)G);
				buffer.put((byte)B);
			}
		}

		// Get buffer ready to be read
		buffer.flip();
		return buffer.array();
	}

	
	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}
	
}
