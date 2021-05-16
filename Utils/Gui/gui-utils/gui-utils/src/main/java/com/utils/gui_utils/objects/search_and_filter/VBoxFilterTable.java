package com.utils.gui_utils.objects.search_and_filter;

import com.utils.gui_utils.CustomControlAbstr;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.alerts.CustomAlertWarning;
import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.gui_utils.factories.LayoutControlsFactory;
import com.utils.gui_utils.objects.HBoxWindowButtons;
import com.utils.gui_utils.objects.combo_box.FilterComboBox;
import com.utils.string.regex.custom_patterns.CustomPatterns;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VBoxFilterTable extends CustomControlAbstr<VBox> {

	private final SearchAndFilterTable searchAndFilterTable;

	private final ComboBox<String> comboBoxFilterColumn;
	private final VBoxPatterns vBoxPatterns;
	private final ComboBox<FilterType> comboBoxFilterType;

	public VBoxFilterTable(
			final SearchAndFilterTable searchAndFilterTable,
			final int columnIndex) {

		this.searchAndFilterTable = searchAndFilterTable;

		comboBoxFilterColumn = FactoryComboBoxColumn.newInstance(searchAndFilterTable, columnIndex);
		vBoxPatterns = new VBoxPatterns(event -> filter());
		comboBoxFilterType = createComboBoxFilterType();
	}

	private static ComboBox<FilterType> createComboBoxFilterType() {

		final ComboBox<FilterType> comboBoxFilterType = new FilterComboBox<>();
		comboBoxFilterType.getItems().addAll(FilterType.values());
		comboBoxFilterType.getSelectionModel().selectFirst();
		return comboBoxFilterType;
	}

	@Override
	protected VBox createRoot() {

		final VBox vBoxRoot = LayoutControlsFactory.createVBox(false);

		final HBox hBoxFilterColumn = createHBoxFilterColumn();
		GuiUtils.addToVBox(vBoxRoot, hBoxFilterColumn,
				Pos.CENTER_LEFT, Priority.NEVER, 12, 7, 7, 11);

		final Label labelFilterPatterns =
				BasicControlsFactory.createLabel("filter patterns:", "bold");
		GuiUtils.addToVBox(vBoxRoot, labelFilterPatterns,
				Pos.CENTER_LEFT, Priority.NEVER, 7, 7, 0, 11);

		GuiUtils.addToVBox(vBoxRoot, vBoxPatterns.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 7, 7, 0, 7);

		final HBox hBoxFilterType = createHBoxFilterType();
		GuiUtils.addToVBox(vBoxRoot, hBoxFilterType,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 7, 11);

		final HBoxWindowButtons hBoxWindowButtons = new HBoxWindowButtons("Filter", this::filter);
		GuiUtils.addToVBox(vBoxRoot, hBoxWindowButtons.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 7, 7, 15, 0);

		return vBoxRoot;
	}

	private HBox createHBoxFilterColumn() {

		final HBox hBoxFilterColumn = LayoutControlsFactory.createHBox();

		final Label labelSearchColumn =
				BasicControlsFactory.createLabel("filter column:", "bold");
		GuiUtils.addToHBox(hBoxFilterColumn, labelSearchColumn,
				Pos.CENTER, Priority.NEVER, 0, 0, 0, 0);

		GuiUtils.addToHBox(hBoxFilterColumn, comboBoxFilterColumn,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 14);

		return hBoxFilterColumn;
	}

	private HBox createHBoxFilterType() {

		final HBox hBoxFilterType = LayoutControlsFactory.createHBox();

		final Label labelDescription = BasicControlsFactory.createLabel("logical operator " +
				"to apply between current filter and previously applied filters:", "bold");
		GuiUtils.addToHBox(hBoxFilterType, labelDescription,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		GuiUtils.addToHBox(hBoxFilterType, comboBoxFilterType,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		return hBoxFilterType;
	}

	private void filter() {

		final int filterColumnIndex =
				comboBoxFilterColumn.getSelectionModel().getSelectedIndex();

		final CustomPatterns customPatterns = vBoxPatterns.createCustomPatterns();
		if (customPatterns != null) {

			if (customPatterns.checkEmptyPatterns()) {
				new CustomAlertWarning("empty filter patterns",
						"All the search patterns above are empty." +
								" It is redundant to perform a search.");

			} else {
				final FilterType filterType = comboBoxFilterType.getSelectionModel().getSelectedItem();
				searchAndFilterTable.filterTable(filterType, filterColumnIndex, customPatterns);

				getRoot().getScene().getWindow().hide();
			}
		}
	}
}
