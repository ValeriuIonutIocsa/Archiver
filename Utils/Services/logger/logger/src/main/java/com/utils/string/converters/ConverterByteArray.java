package com.utils.string.converters;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class ConverterByteArray {

	private static final Charset ENCODING = StandardCharsets.UTF_8;

	private ConverterByteArray() {
	}

	public static String byteArrayToString(
			final byte[] bytes) {

		final byte[] encodedBytes = Base64.getEncoder().encode(bytes);
		return new String(encodedBytes, ENCODING);
	}

	public static byte[] parseByteArray(
			final String str) {

		final byte[] encodedBytes = str.getBytes(ENCODING);
		return Base64.getDecoder().decode(encodedBytes);
	}
}
