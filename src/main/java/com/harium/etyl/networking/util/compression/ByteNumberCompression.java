package com.harium.etyl.networking.util.compression;

public class ByteNumberCompression {

	private static final int BASE = 256;
	
	public static byte[] encode(int number) {
		return encode(number, 2);
	}

	public static byte[] encode(int number, int bytes) {

		int multiplier = 1;
		
		byte[] encodedNumber = new byte[bytes];

		for(int i=0; i<encodedNumber.length; i++){

			long numb = (number/multiplier);

			if(numb > 0) {
				encodedNumber[i] = (byte)(numb-128);
			} else {
				encodedNumber[i] = -128;
			}

			multiplier *= BASE;
		}
		
		return encodedNumber;

	}

	public static int decode(byte ... encodedNumber) {

		final int BASE = 256;
		int multiplier = 1;

		int number = 0;
		int it = 0;

		for(int i = 0; i<encodedNumber.length;i++){

			it = encodedNumber[i]+128;

			number += it*multiplier;

			multiplier*=BASE;
		}

		return number;	
	}
}
