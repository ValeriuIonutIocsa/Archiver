package com.utils.string.compression;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class GzipCompressionUtilsTest {

	@Test
	void testCompress() {

		final String input = "ABC BCD CBD EDC";
		final byte[] bytes = input.getBytes(StandardCharsets.UTF_8);

		final byte[] compressedBytes = GzipCompressionUtils.compress(bytes);
		final byte[] decompressedBytes = GzipCompressionUtils.decompress(compressedBytes);
		assertArrayEquals(bytes, decompressedBytes);
	}
}
