package com.utils.gui_utils.objects.list_select;

import java.util.List;

import com.utils.annotations.ApiMethod;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.gui_utils.CustomControlAbstr;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.gui_utils.factories.LayoutControlsFactory;
import com.utils.gui_utils.objects.tables.table_view.CustomTableView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

abstract class CustomListSelectionView<
		TableRowDataTLeft extends TableRowData,
		TableRowDataTRight extends TableRowData>
		extends CustomControlAbstr<GridPane> {

	private final double leftSizeRatio;

	private final CustomTableView<TableRowDataTLeft> customTableViewLeft;
	private final CustomTableView<TableRowDataTRight> customTableViewRight;
	private final Button buttonMoveToRight;
	private final Button buttonMoveAllToRight;
	private final Button buttonMoveToLeft;
	private final Button buttonMoveAllToLeft;

	CustomListSelectionView(
			final double leftSizeRatio,
			final List<TableRowDataTLeft> leftItemList,
			final List<TableRowDataTRight> rightItemList) {

		this.leftSizeRatio = leftSizeRatio;

		customTableViewLeft = createCustomTableViewLeft(leftItemList);
		customTableViewRight = createCustomTableViewRight(rightItemList);
		buttonMoveToRight = createButton(">", event -> moveToRight());
		buttonMoveAllToRight = createButton(">>", event -> moveAllToRight());
		buttonMoveToLeft = createButton("<", event -> moveToLeft());
		buttonMoveAllToLeft = createButton("<<", event -> moveAllToLeft());
		updateButtons();
	}

	protected CustomTableView<TableRowDataTLeft> createCustomTableViewLeft(
			final List<TableRowDataTLeft> leftTableRowDataList) {

		final TableColumnData[] tableColumnDataArrayLeft = getTableColumnDataArrayLeft();
		final CustomTableView<TableRowDataTLeft> customTableViewLeft =
				new CustomTableView<>(tableColumnDataArrayLeft, false, true, 0);

		customTableViewLeft.getSelectionModel().getSelectedCells()
				.addListener(new ListChangeListenerListSelectionView<>(this));

		customTableViewLeft.setItems(leftTableRowDataList);
		return customTableViewLeft;
	}

	protected abstract TableColumnData[] getTableColumnDataArrayLeft();

	protected CustomTableView<TableRowDataTRight> createCustomTableViewRight(
			final List<TableRowDataTRight> rightTableRowDataList) {

		final TableColumnData[] tableColumnDataArrayRight = getTableColumnDataArrayRight();
		final CustomTableView<TableRowDataTRight> customTableViewRight =
				new CustomTableView<>(tableColumnDataArrayRight, false, true, 0);

		customTableViewRight.getSelectionModel().getSelectedCells()
				.addListener(new ListChangeListenerListSelectionView<>(this));

		customTableViewRight.setItems(rightTableRowDataList);
		return customTableViewRight;
	}

	protected abstract TableColumnData[] getTableColumnDataArrayRight();

	private static Button createButton(
			final String text,
			final EventHandler<ActionEvent> eventHandler) {

		final Button button = BasicControlsFactory.createButton(text);
		button.setMaxWidth(Double.MAX_VALUE);
		button.setOnAction(eventHandler);
		button.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 12));
		return button;
	}

	@Override
	protected GridPane createRoot() {

		final GridPane gridPaneRoot = LayoutControlsFactory.createGridPane();

		gridPaneRoot.getColumnConstraints().add(
				LayoutControlsFactory.createPercentageConstraints(leftSizeRatio));
		gridPaneRoot.getColumnConstraints().add(
				LayoutControlsFactory.createPercentageConstraints(1 - leftSizeRatio));

		final VBox vBoxLeft = createVBoxLeft();
		GuiUtils.addToGridPane(gridPaneRoot, vBoxLeft, 0, 0, 1, 1,
				Pos.CENTER_LEFT, Priority.ALWAYS, Priority.NEVER, 0, 0, 0, 0);

		final VBox vBoxRight = createVBoxRight();
		GuiUtils.addToGridPane(gridPaneRoot, vBoxRight, 1, 0, 1, 1,
				Pos.CENTER_LEFT, Priority.ALWAYS, Priority.NEVER, 0, 0, 0, 0);

		return gridPaneRoot;
	}

	private VBox createVBoxLeft() {

		final VBox vBoxLeft = LayoutControlsFactory.createVBox();

		addLeftHeader(vBoxLeft);

		final HBox hBoxLeft = createHBoxLeft();
		GuiUtils.addToVBox(vBoxLeft, hBoxLeft,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		return vBoxLeft;
	}

	@ApiMethod
	protected void addLeftHeader(
			final VBox vBoxLeft) {
	}

	private HBox createHBoxLeft() {

		final HBox hBoxLeft = LayoutControlsFactory.createHBox();

		GuiUtils.addToHBox(hBoxLeft, customTableViewLeft,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final VBox vBoxButtons = createVBoxButtons();
		GuiUtils.addToHBox(hBoxLeft, vBoxButtons,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		return hBoxLeft;
	}

	private VBox createVBoxRight() {

		final VBox vBoxRight = LayoutControlsFactory.createVBox();

		addRightHeader(vBoxRight);

		GuiUtils.addToVBox(vBoxRight, customTableViewRight,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		return vBoxRight;
	}

	@ApiMethod
	protected void addRightHeader(
			final VBox vBoxRight) {
	}

	private VBox createVBoxButtons() {

		final VBox vBoxButtons = LayoutControlsFactory.createVBox();

		GuiUtils.addToVBox(vBoxButtons, buttonMoveToRight,
				Pos.CENTER_LEFT, Priority.NEVER, 7, 0, 7, 0);

		GuiUtils.addToVBox(vBoxButtons, buttonMoveAllToRight,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 7, 0);

		GuiUtils.addToVBox(vBoxButtons, buttonMoveToLeft,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 7, 0);

		GuiUtils.addToVBox(vBoxButtons, buttonMoveAllToLeft,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 7, 0);

		return vBoxButtons;
	}

	abstract void moveToRight();

	abstract void moveAllToRight();

	abstract void moveToLeft();

	abstract void moveAllToLeft();

	void updateButtons() {

		buttonMoveToRight.setDisable(customTableViewLeft.getSelectionModel().getSelectedIndices().isEmpty());
		buttonMoveAllToRight.setDisable(customTableViewLeft.getItems().isEmpty());
		buttonMoveToLeft.setDisable(customTableViewRight.getSelectionModel().getSelectedIndices().isEmpty());
		buttonMoveAllToLeft.setDisable(customTableViewRight.getItems().isEmpty());
		getRoot().requestFocus();
	}

	@ApiMethod
	public CustomTableView<TableRowDataTLeft> getCustomTableViewLeft() {
		return customTableViewLeft;
	}

	@ApiMethod
	public CustomTableView<TableRowDataTRight> getCustomTableViewRight() {
		return customTableViewRight;
	}

	Button getButtonMoveToRight() {
		return buttonMoveToRight;
	}

	Button getButtonMoveAllToRight() {
		return buttonMoveAllToRight;
	}

	Button getButtonMoveToLeft() {
		return buttonMoveToLeft;
	}

	Button getButtonMoveAllToLeft() {
		return buttonMoveAllToLeft;
	}
}
