package com.utils.data_types.data_items.objects.date;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.utils.data_types.data_items.AbstractDataItem;
import com.utils.string.converters.ConverterDate;

public class DataItemDate extends AbstractDataItem<Date> implements Comparable<DataItemDate> {

	private final Date value;

	DataItemDate(
			final Date value) {

		this.value = value;
	}

	@Override
	public void serializeToDataBase(
			final int index,
			final PreparedStatement preparedStatement) throws SQLException {

		final Timestamp timestamp = new Timestamp(value.getTime());
		final Calendar calendar = Calendar.getInstance();
		preparedStatement.setTimestamp(index, timestamp, calendar);
	}

	@Override
	public int compareTo(
			final DataItemDate other) {
		return value.compareTo(other.value);
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
		return ConverterDate.dateToString(value);
	}

	@Override
	public Date getValue() {
		return value;
	}
}
