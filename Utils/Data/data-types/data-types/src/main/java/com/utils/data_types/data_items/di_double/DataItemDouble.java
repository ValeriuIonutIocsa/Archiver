package com.utils.data_types.data_items.di_double;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.utils.data_types.data_items.AbstractDataItem;
import com.utils.string.StrUtils;

public class DataItemDouble extends AbstractDataItem<Double> implements Comparable<DataItemDouble> {

	private final double value;
	private final int digits;

	DataItemDouble(
			final double value,
			final int digits) {

		this.value = value;
		this.digits = digits;
	}

	@Override
	public void serializeToDataBase(
			final int index,
			final PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setDouble(index, value);
	}

	@Override
	public String createCopyString() {
		return StrUtils.doubleToString(value, 0, digits, false);
	}

	@Override
	public int compareTo(
			final DataItemDouble other) {

		final int result;
		if (Double.isNaN(value)) {
			if (Double.isNaN(other.value)) {
				result = 0;
			} else {
				result = -1;
			}
		} else {
			if (Double.isNaN(other.value)) {
				result = 1;
			} else {
				result = Double.compare(value, other.value);
			}
		}
		return result;
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

	@Override
	public String toString() {
		return StrUtils.doubleToString(value, 0, digits, true);
	}

	@Override
	public Double getValue() {
		return value;
	}
}
