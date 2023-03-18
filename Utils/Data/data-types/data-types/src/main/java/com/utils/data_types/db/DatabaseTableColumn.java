package com.utils.data_types.db;

import com.utils.string.StrUtils;

public class DatabaseTableColumn {

	private final String name;
	private final String type;

	public DatabaseTableColumn(
			final String name,
			final String type) {

		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
}
