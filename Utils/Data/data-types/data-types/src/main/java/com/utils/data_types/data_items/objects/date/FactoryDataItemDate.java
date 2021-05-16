package com.utils.data_types.data_items.objects.date;

import java.util.Date;

public final class FactoryDataItemDate {

	private FactoryDataItemDate() {
	}

	public static DataItemDate newInstance(
			final Date date) {

		DataItemDate dataItemDate = null;
		if (date != null) {
			dataItemDate = new DataItemDate(date);
		}
		return dataItemDate;
	}
}
