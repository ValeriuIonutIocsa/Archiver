package com.utils.gui_utils.objects.list_view;

import com.utils.gui_utils.GuiUtils;

import javafx.scene.control.ListCell;

public abstract class CustomListCell<
		ObjectT> extends ListCell<ObjectT> {

	@Override
	protected void updateItem(
			final ObjectT item,
			final boolean empty) {

		super.updateItem(item, empty);

		if (empty || item == null) {
			GuiUtils.updateEmptyCell(this);
		} else {
			updateNonEmptyItem(item);
		}
	}

	protected abstract void updateNonEmptyItem(
			ObjectT item);
}
