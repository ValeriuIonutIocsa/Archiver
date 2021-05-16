package com.utils.gui_utils.objects.tables.tree_table;

import com.utils.annotations.ApiMethod;
import com.utils.data_types.data_items.DataItem;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.LayoutControlsFactory;
import com.utils.gui_utils.objects.tables.CustomCell;
import com.utils.gui_utils.version.VersionDependentMethods;
import com.utils.log.Logger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableRow;
import javafx.scene.layout.StackPane;

public abstract class CustomTreeTableCell<
		RowDataT,
		CellDataT>
		extends TreeTableCell<RowDataT, CellDataT> implements CustomCell<CellDataT> {

	@Override
	protected void updateItem(
			final CellDataT item,
			final boolean empty) {

		super.updateItem(item, empty);

		final double leftPadding = VersionDependentMethods.computeTreeTableCellLeftPadding(this);
		setPadding(new Insets(0, 0, 0, leftPadding));
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		final StackPane stackPane = LayoutControlsFactory.createStackPane(Pos.CENTER_LEFT);
		setGraphic(stackPane);

		if (empty || item == null) {
			updateEmptyCell(stackPane);
		} else {
			updateNonEmptyCell(item, stackPane);
		}
	}

	@Override
	public StackPane updateEmptyCell(
			final StackPane stackPane) {

		setContextMenu(null);
		return stackPane;
	}

	@Override
	public StackPane updateNonEmptyCell(
			final CellDataT item,
			final StackPane stackPane) {

		try {
			setText(stackPane, item);

			final ContextMenu contextMenu = createContextMenu(item);
			CustomCell.setContextMenu(this, stackPane, contextMenu);

		} catch (final Exception exc) {
			Logger.printError("failed to render tree table cell!");
			Logger.printException(exc);
		}
		return stackPane;
	}

	@Override
	public void setText(
			final StackPane stackPane,
			final CellDataT item) {

		final Label label = new Label(item.toString());

		final String[] labelStyleClassElements = getLabelStyleClassElements();
		if (labelStyleClassElements != null) {
			label.getStyleClass().addAll(labelStyleClassElements);
		}

		final ContentDisplay labelContentDisplay = getLabelContentDisplay();
		label.setContentDisplay(labelContentDisplay);
		final Node labelGraphic = createLabelGraphic(item);
		label.setGraphic(labelGraphic);

		final Pos textAlignment = getTextAlignmentValue();
		GuiUtils.addToStackPane(stackPane, label, textAlignment, 1, 1, 1, 1);
	}

	@Override
	public String[] getLabelStyleClassElements() {
		return null;
	}

	@Override
	public ContentDisplay getLabelContentDisplay() {
		return ContentDisplay.LEFT;
	}

	@Override
	public Node createLabelGraphic(
			final CellDataT item) {
		return null;
	}

	@Override
	public Pos getTextAlignmentValue() {
		return Pos.CENTER_LEFT;
	}

	@Override
	public ContextMenu createContextMenu(
			final CellDataT item) {
		return null;
	}

	@ApiMethod
	protected RowDataT getRowData() {

		RowDataT rowData = null;
		final TreeTableRow<RowDataT> treeTableRow = getTreeTableRow();
		if (treeTableRow != null) {
			rowData = treeTableRow.getItem();
		}
		return rowData;
	}

	@ApiMethod
	protected <
			CellValue> CellValue getCellData(
					final Class<CellValue> cellValueClass) {

		CellValue cellData = null;
		final CellDataT item = getItem();
		if (item instanceof DataItem<?>) {

			final DataItem<?> dataItem = (DataItem<?>) item;
			final Object value = dataItem.getValue();
			if (cellValueClass.isInstance(value)) {
				cellData = cellValueClass.cast(value);
			}
		}
		return cellData;
	}

	@ApiMethod
	protected void collapseTreeViewToLevel() {

		final TreeItem<RowDataT> treeItem = getTreeTableRow().getTreeItem();
		final int depthInTreeView = getTreeTableView().getTreeItemLevel(treeItem);
		collapseTreeViewToLevel(depthInTreeView);
	}

	@ApiMethod
	protected void collapseTreeViewToLevel(
			final int depthInTreeView) {

		final TreeItem<RowDataT> treeItemRoot = getTreeTableView().getRoot();
		collapseTreeViewToLevelRec(depthInTreeView, treeItemRoot, 0);
	}

	private void collapseTreeViewToLevelRec(
			final int collapseDepth,
			final TreeItem<RowDataT> treeItem,
			final int depth) {

		if (depth >= collapseDepth) {
			treeItem.setExpanded(false);
		}

		final int childDepth = depth + 1;
		for (final TreeItem<RowDataT> treeItemChild : treeItem.getChildren()) {
			collapseTreeViewToLevelRec(collapseDepth, treeItemChild, childDepth);
		}
	}
}
