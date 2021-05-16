package com.utils.gui_utils.objects.tables.tree_table;

import com.utils.data_types.table.TableRowDataParentChild;

import javafx.css.PseudoClass;

public class CustomTreeTableCellParentChild<
		TableRowDataT extends TableRowDataParentChild>
		extends CustomTreeTableCell<TableRowDataT, Object> {

	private final static PseudoClass PSEUDO_CLASS_PARENT = PseudoClass.getPseudoClass("parent");

	@Override
	protected void updateItem(
			final Object item,
			final boolean empty) {

		super.updateItem(item, empty);

		final boolean generic = checkGeneric();
		pseudoClassStateChanged(PSEUDO_CLASS_PARENT, generic);
	}

	private boolean checkGeneric() {

		boolean generic = false;
		final TableRowDataT tableRowData = getRowData();
		if (tableRowData != null) {
			generic = tableRowData.isParent();
		}
		return generic;
	}
}
