package com.utils.gui_utils.objects.search_and_filter;

import java.util.Collection;

import com.utils.gui_utils.objects.combo_box.FilterComboBox;

import javafx.scene.control.ComboBox;

final class FactoryComboBoxColumn {

	private FactoryComboBoxColumn() {
	}

	public static ComboBox<String> newInstance(
			final SearchAndFilterTable searchAndFilterTable,
			final int columnIndex) {

		final ComboBox<String> comboBoxColumn = new FilterComboBox<>();

		comboBoxColumn.setMaxWidth(200);
		final Collection<String> columnNames = searchAndFilterTable.getTableColumnNames();
		comboBoxColumn.getItems().addAll(columnNames);
		comboBoxColumn.getSelectionModel().select(columnIndex);

		return comboBoxColumn;
	}
}
