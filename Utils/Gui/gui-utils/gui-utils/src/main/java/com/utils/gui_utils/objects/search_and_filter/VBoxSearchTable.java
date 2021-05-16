package com.utils.gui_utils.objects.search_and_filter;

import com.utils.gui_utils.CustomControlAbstr;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.alerts.CustomAlertWarning;
import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.gui_utils.factories.LayoutControlsFactory;
import com.utils.gui_utils.objects.HBoxWindowButtons;
import com.utils.string.regex.custom_patterns.CustomPatterns;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VBoxSearchTable extends CustomControlAbstr<VBox> {

	private final SearchAndFilterTable searchAndFilterTable;

	private final ComboBox<String> comboBoxSearchColumn;
	private final VBoxPatterns vBoxPatterns;
	private final CheckBox checkBoxKeepWindowOpen;

	public VBoxSearchTable(
			final SearchAndFilterTable searchAndFilterTable,
			final int columnIndex) {

		this.searchAndFilterTable = searchAndFilterTable;

		comboBoxSearchColumn = FactoryComboBoxColumn.newInstance(searchAndFilterTable, columnIndex);
		vBoxPatterns = new VBoxPatterns(event -> search());
		checkBoxKeepWindowOpen = createCheckBoxKeepWindowOpen();
	}

	private static CheckBox createCheckBoxKeepWindowOpen() {

		final CheckBox checkBoxKeepWindowOpen = BasicControlsFactory.createCheckBox("keep window open");
		checkBoxKeepWindowOpen.setTooltip(BasicControlsFactory.createTooltip(
				"keep the search window open to be able" +
						System.lineSeparator() + "to search for the same pattern multiple times"));
		return checkBoxKeepWindowOpen;
	}

	@Override
	protected VBox createRoot() {

		final VBox vBoxRoot = LayoutControlsFactory.createVBox(false);

		final HBox hBoxSearchColumn = createHBoxSearchColumn();
		GuiUtils.addToVBox(vBoxRoot, hBoxSearchColumn,
				Pos.CENTER_LEFT, Priority.NEVER, 12, 7, 7, 11);

		final Label labelSearchPatterns =
				BasicControlsFactory.createLabel("search patterns:", "bold");
		GuiUtils.addToVBox(vBoxRoot, labelSearchPatterns,
				Pos.CENTER_LEFT, Priority.NEVER, 7, 7, 0, 11);

		GuiUtils.addToVBox(vBoxRoot, vBoxPatterns.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 7, 7, 0, 7);

		GuiUtils.addToVBox(vBoxRoot, checkBoxKeepWindowOpen,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 7, 11);

		final HBoxWindowButtons hBoxWindowButtons = new HBoxWindowButtons("Search", this::search);
		GuiUtils.addToVBox(vBoxRoot, hBoxWindowButtons.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 7, 7, 15, 0);

		return vBoxRoot;
	}

	private HBox createHBoxSearchColumn() {

		final HBox hBoxSearchColumn = LayoutControlsFactory.createHBox();

		final Label labelSearchColumn =
				BasicControlsFactory.createLabel("search column:", "bold");
		GuiUtils.addToHBox(hBoxSearchColumn, labelSearchColumn,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		GuiUtils.addToHBox(hBoxSearchColumn, comboBoxSearchColumn,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 14);

		return hBoxSearchColumn;
	}

	private void search() {

		final int searchColumnIndex =
				comboBoxSearchColumn.getSelectionModel().getSelectedIndex();

		final CustomPatterns customPatterns = vBoxPatterns.createCustomPatterns();
		if (customPatterns != null) {

			if (customPatterns.checkEmptyPatterns()) {
				new CustomAlertWarning("empty search patterns",
						"All the search patterns above are empty." +
								" It is redundant to perform a search.");

			} else {
				searchAndFilterTable.searchTable(searchColumnIndex, customPatterns);

				if (!checkBoxKeepWindowOpen.isSelected()) {
					getRoot().getScene().getWindow().hide();
				}
			}
		}
	}
}
