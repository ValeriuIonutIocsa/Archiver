package com.utils.gui_utils.version;

import com.utils.gui_utils.factories.BasicControlsFactory;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.control.skin.VirtualFlow;
import javafx.stage.Stage;

public final class VersionDependentMethods {

	private VersionDependentMethods() {
	}

	public static void disableTableViewColumnReordering(
			final TableView<?> tableView) {

		for (final TableColumn<?, ?> tableColumn : tableView.getColumns()) {
			tableColumn.setReorderable(false);
		}
	}

	public static void disableTreeTableViewColumnReordering(
			final TreeTableView<?> treeTableView) {

		for (final TreeTableColumn<?, ?> tableColumn : treeTableView.getColumns()) {
			tableColumn.setReorderable(false);
		}
	}

	public static IndexedCell<?> getControlIndexedCell(
			final Control control,
			final int index) {

		if (control.getScene() == null) {

			final Group group = new Group();

			final Scene scene = new Scene(group);
			group.getChildren().setAll(control);

			final Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
		}

		final VirtualFlow<?> virtualFlow = (VirtualFlow<?>) control.lookup("#virtual-flow");
		return virtualFlow.getCell(index);
	}

	public static Tooltip createTooltip(
			final String text) {

		final Tooltip tooltip = new Tooltip(text);
		tooltip.setShowDelay(BasicControlsFactory.TOOLTIP_SHOW_DELAY);
		tooltip.setShowDuration(BasicControlsFactory.TOOLTIP_SHOW_TIME);
		tooltip.setHideDelay(BasicControlsFactory.TOOLTIP_CLOSE_DELAY);
		return tooltip;
	}

	public static void setupCustomTooltipBehavior() {
	}

	public static ListView<?> getComboBoxListView(
			final ComboBox<?> comboBox) {

		ListView<?> comboBoxListView = null;
		final Skin<?> skin = comboBox.getSkin();
		if (skin instanceof ComboBoxListViewSkin<?>) {

			final ComboBoxListViewSkin<?> comboBoxListViewSkin = (ComboBoxListViewSkin<?>) skin;
			final Node popupContent = comboBoxListViewSkin.getPopupContent();
			if (popupContent instanceof ListView<?>) {
				comboBoxListView = (ListView<?>) popupContent;
			}
		}
		return comboBoxListView;
	}

	public static int computeTreeTableCellLeftPadding(
			final TreeTableCell<?, ?> treeTableCell) {

		final int leftPadding;
		final int columnIndex = treeTableCell.getTreeTableView().getColumns()
				.indexOf(treeTableCell.getTableColumn());
		if (columnIndex == 0) {

			final TreeItem<?> treeItem = treeTableCell.getTreeTableRow().getTreeItem();
			final int depthInTreeView = treeTableCell.getTreeTableView().getTreeItemLevel(treeItem);
			leftPadding = 8 + 10 * depthInTreeView;

		} else {
			leftPadding = 0;
		}
		return leftPadding;
	}
}
