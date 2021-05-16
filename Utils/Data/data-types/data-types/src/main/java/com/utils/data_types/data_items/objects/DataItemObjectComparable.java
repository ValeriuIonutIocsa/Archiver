package com.utils.data_types.data_items.objects;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class DataItemObjectComparable<
		ObjectT extends Comparable<ObjectT>>
		extends DataItemObject<ObjectT> {

	DataItemObjectComparable(
			final ObjectT value,
			final int maxLength) {
		super(value, maxLength);
	}

	@Override
	public int compareTo(
			final DataItemObject<?> other) {

		return new CompareToBuilder()
				.append(value, other.value)
				.toComparison();
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(
			final Object obj) {

		final boolean result;
		if (getClass().isInstance(obj)) {
			result = compareTo(getClass().cast(obj)) == 0;
		} else {
			result = false;
		}
		return result;
	}
}
