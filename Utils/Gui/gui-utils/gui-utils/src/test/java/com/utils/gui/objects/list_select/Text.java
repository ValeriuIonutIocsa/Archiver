package com.utils.gui.objects.list_select;

import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.data_items.objects.FactoryDataItemObjectComparable;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.string.StrUtils;

class Text implements TableRowData {

	private static final long serialVersionUID = -1441603316586307547L;

	static final TableColumnData[] COLUMNS = {
			new TableColumnData("Text", "Text", 1)
	};

	@Override
	public DataItem<?>[] getTableViewDataItemArray() {
		return new DataItem<?>[] {
				FactoryDataItemObjectComparable.newInstance(text)
		};
	}

	private final String text;

	Text(
			final String text) {

		this.text = text;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	String getText() {
		return text;
	}
}
