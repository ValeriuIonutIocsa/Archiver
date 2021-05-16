package com.utils.data_types.data_items;

import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractDataItem<
		ObjectT> implements DataItem<ObjectT> {

	@Override
	public String createCsvString() {
		return createCopyString();
	}

	@Override
	public String createCopyString() {
		return toString();
	}

	@Override
	public void writeToXml(
			final XmlStAXWriter xmlStAXWriter,
			final String columnTitleName) {

		final String string = createCopyString();
		xmlStAXWriter.writeAttribute(columnTitleName, string);
	}

	@Override
	public ObjectT createXlsxValue() {
		return getValue();
	}
}
