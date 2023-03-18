package com.utils.data_types;

import java.io.Serializable;
import java.util.Objects;

import com.utils.data_types.table.TableColumnData;
import com.utils.string.StrUtils;

public class DataInfo implements Serializable {

	private static final long serialVersionUID = 3078827620097666118L;

	private final String option;
	private final String displayName;
	private final String xmlRootElementTagName;
	private final String xmlDataElementTagName;
	private final TableColumnData[] columnsData;
	private final TableColumnData[] columnsTable;

	public DataInfo(
			final String option,
			final String displayName,
			final String xmlRootElementTagName,
			final String xmlDataElementTagName,
			final TableColumnData[] columns) {
		this(option, displayName, xmlRootElementTagName, xmlDataElementTagName, columns, columns);
	}

	public DataInfo(
			final String option,
			final String displayName,
			final String xmlRootElementTagName,
			final String xmlDataElementTagName,
			final TableColumnData[] columnsData,
			final TableColumnData[] columnsTable) {

		this.option = option;
		this.displayName = displayName;
		this.xmlRootElementTagName = xmlRootElementTagName;
		this.xmlDataElementTagName = xmlDataElementTagName;
		this.columnsData = columnsData;
		this.columnsTable = columnsTable;
	}

	public DataInfo cloneOtherDisplayName(
			final String displayName) {
		return new DataInfo(option, displayName, xmlRootElementTagName, xmlDataElementTagName,
				columnsData, columnsTable);
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(
			final Object o) {

		boolean result = false;
		if (o instanceof DataInfo) {

			final DataInfo other = (DataInfo) o;
			result = Objects.equals(option, other.option);
		}
		return result;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public String getOption() {
		return option;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getXmlRootElementTagName() {
		return xmlRootElementTagName;
	}

	public String getXmlDataElementTagName() {
		return xmlDataElementTagName;
	}

	public TableColumnData[] getColumnsData() {
		return columnsData;
	}

	public TableColumnData[] getColumnsTable() {
		return columnsTable;
	}
}
