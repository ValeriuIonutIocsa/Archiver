package com.utils.data_types.table;

import java.io.PrintStream;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.utils.data_types.data_items.DataItem;
import com.utils.xml.stax.XmlStAXWriter;

public interface TableRowData extends Serializable {

	default DataItem<?>[] getDataItems() {
		return getTableViewData();
	}

	DataItem<?>[] getTableViewData();

	default void writeToXml(
			final String tagName,
			final TableColumnData[] columnsData,
			final XmlStAXWriter xmlStAXWriter) {

		xmlStAXWriter.writeStartElement(tagName);

		final DataItem<?>[] dataItems = getDataItems();

		for (int i = 0; i < columnsData.length; i++) {

			final TableColumnData tableColumnData = columnsData[i];
			final String columnName = tableColumnData.getSerializeName();
			if (StringUtils.isNotBlank(columnName)) {

				final DataItem<?> dataItem = dataItems[i];
				if (dataItem != null) {
					dataItem.writeToXml(xmlStAXWriter, columnName);
				} else {
					xmlStAXWriter.writeAttribute(columnName, "");
				}
			}
		}
		xmlStAXWriter.writeEndElement(tagName);
	}

	default void writeToCsv(
			final PrintStream printStream) {

		final DataItem<?>[] dataItems = getDataItems();
		for (int i = 0; i < dataItems.length; i++) {

			final DataItem<?> dataItem = dataItems[i];

			if (dataItem != null) {
				final String csvString = dataItem.createCsvString();
				printStream.print(csvString);
			}
			if (i < dataItems.length - 1) {
				printStream.print(',');
			}
		}
		printStream.println();
	}
}
