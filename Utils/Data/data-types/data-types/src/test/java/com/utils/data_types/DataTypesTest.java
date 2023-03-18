package com.utils.data_types;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.string.StrUtils;

class DataTypesTest {

	@Test
	void testSignExtendTo32Positive() {

		final int valueO = 0b00000000000000000000000101010111;
		final int valueE = 0b00000000000000000000000101010111;

		final int signExtendedValue = DataTypes.signExtendTo32(valueO, 10);
		Assertions.assertEquals(valueE, signExtendedValue);
	}

	@Test
	void testSignExtendTo32Negative() {

		final int valueO = 0b00000000000000000000001101010111;
		final int valueE = 0b11111111111111111111111101010111;

		final int signExtendedValue = DataTypes.signExtendTo32(valueO, 10);
		Assertions.assertEquals(valueE, signExtendedValue);
	}

	@Test
	void parseUnsignedLittleEndianBase128() {

		final byte[] byteArray;
		final long expectedResult;
		final int input = Integer.parseInt("1");
		if (input == 1) {
			byteArray = StrUtils.tryParseByteArrayFromHexString("b0 20");
			expectedResult = 4_144;

		} else if (input == 2) {
			byteArray = StrUtils.tryParseByteArrayFromHexString("b4 20");
			expectedResult = 4_148;

		} else if (input == 3) {
			byteArray = StrUtils.tryParseByteArrayFromHexString("84 84 x2");
			expectedResult = 32_772;

		} else {
			byteArray = StrUtils.tryParseByteArrayFromHexString("12");
			expectedResult = 18;
		}

		final ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
		final long value = DataTypes.parseUnsignedLittleEndianBase128(byteBuffer);
		Assertions.assertEquals(expectedResult, value);
	}
}
