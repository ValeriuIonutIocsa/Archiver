package com.utils.data_types.db;

import com.utils.string.StrUtils;

public class DatabaseTableInfo {

	private final String name;
	private final DatabaseTableColumn[] databaseTableColumnArray;
	private final String[] constraintArray;

	public DatabaseTableInfo(
			final String name,
			final DatabaseTableColumn[] databaseTableColumnArray,
			final String[] constraintArray) {

		this.name = name;
		this.databaseTableColumnArray = databaseTableColumnArray;
		this.constraintArray = constraintArray;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public String getName() {
		return name;
	}

	public DatabaseTableColumn[] getDatabaseTableColumnArray() {
		return databaseTableColumnArray;
	}

	public String[] getConstraintArray() {
		return constraintArray;
	}
}
