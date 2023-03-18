package com.utils.gui.objects.messages.data;

import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.data_items.objects.FactoryDataItemObjectComparable;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.log.messages.MessageType;
import com.utils.string.StrUtils;

public class TableRowDataMessage implements TableRowData {

	private static final long serialVersionUID = -4482668257460205647L;

	public static final TableColumnData[] COLUMNS = {
			new TableColumnData("Messages", "Messages", 1.0)
	};

	@Override
	public DataItem<?>[] getTableViewDataItemArray() {
		return new DataItem<?>[] {
				FactoryDataItemObjectComparable.newInstance(text)
		};
	}

	private final boolean category;
	private final String text;
	private final MessageType type;

	public TableRowDataMessage(
			final boolean category,
			final MessageType type,
			final String text) {

		this.category = category;
		this.text = text;
		this.type = type;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public boolean isCategory() {
		return category;
	}

	public MessageType getType() {
		return type;
	}
}
