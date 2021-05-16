package com.utils.gui_utils.objects.messages;

import java.util.Comparator;
import java.util.List;

import com.utils.data_types.table.TableColumnData;
import com.utils.gui_utils.CustomControlAbstr;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.gui_utils.factories.LayoutControlsFactory;
import com.utils.gui_utils.objects.messages.data.TableRowDataMessage;
import com.utils.gui_utils.objects.split_pane.CustomSplitPane;
import com.utils.gui_utils.objects.tables.tree_table.CustomTreeTableView;
import com.utils.gui_utils.objects.tables.tree_table.UnfilteredTreeItem;
import com.utils.log.messages.Message;
import com.utils.log.messages.MessageType;
import com.utils.log.messages.Messages;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SplitPaneMessages extends CustomControlAbstr<SplitPane> {

	private final UserActionRequiredHelper userActionRequiredHelper;

	private final Label labelCriticalCount;
	private final Label labelWarningCount;
	private final Label labelInfoCount;
	private final CustomTreeTableView<TableRowDataMessage> customTreeTableView;

	public SplitPaneMessages(
			final UserActionRequiredHelper userActionRequiredHelper) {

		this.userActionRequiredHelper = userActionRequiredHelper;

		labelCriticalCount = BasicControlsFactory.createLabel("", "bold");
		labelWarningCount = BasicControlsFactory.createLabel("", "bold");
		labelInfoCount = BasicControlsFactory.createLabel("", "bold");
		customTreeTableView = createCustomTreeTableView();
	}

	public void setTopComponent(
			final Parent topComponent) {

		final SplitPane splitPaneRoot = getRoot();
		splitPaneRoot.getItems().remove(0);
		splitPaneRoot.getItems().add(0, topComponent);
		splitPaneRoot.setDividerPositions(0.7);
	}

	private CustomTreeTableView<TableRowDataMessage> createCustomTreeTableView() {

		final TableColumnData[] tableColumnDataArray = TableRowDataMessage.COLUMNS;
		final CustomTreeTableView<TableRowDataMessage> customTreeTableView =
				new CustomTreeTableView<>(tableColumnDataArray, true, true, true, true, 0);
		customTreeTableView.setId("tree-table-view-messages");

		customTreeTableView.getColumnList().get(0)
				.setCellFactory(param -> new CustomTreeTableCellMessages(userActionRequiredHelper));

		return customTreeTableView;
	}

	@Override
	protected SplitPane createRoot() {

		final SplitPane splitPaneRoot = new CustomSplitPane(Orientation.VERTICAL);
		splitPaneRoot.getStylesheets().add("com/utils/gui_utils/objects/messages/split_pane_messages.css");
		splitPaneRoot.setBackground(Background.EMPTY);
		splitPaneRoot.setPadding(new Insets(0));

		splitPaneRoot.getItems().add(new Region());

		final VBox vBoxBottom = createVBoxBottom();
		splitPaneRoot.getItems().add(vBoxBottom);

		splitPaneRoot.setDividerPositions(0.7);

		return splitPaneRoot;
	}

	private VBox createVBoxBottom() {

		final VBox vBoxBottom = LayoutControlsFactory.createVBox();

		final HBox hBoxTop = createHBoxTop();
		GuiUtils.addToVBox(vBoxBottom, hBoxTop,
				Pos.CENTER_LEFT, Priority.NEVER, 5, 0, 5, 0);

		GuiUtils.addToVBox(vBoxBottom, customTreeTableView,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		return vBoxBottom;
	}

	private HBox createHBoxTop() {

		final HBox hBoxTop = LayoutControlsFactory.createHBox();

		GuiUtils.addToHBox(hBoxTop, labelCriticalCount,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		GuiUtils.addToHBox(hBoxTop, labelWarningCount,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		GuiUtils.addToHBox(hBoxTop, labelInfoCount,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		return hBoxTop;
	}

	public void setData(
			final Messages messages) {

		final int criticalCount = messages.computeMessageCount(MessageType.CRITICAL);
		labelCriticalCount.setText(criticalCount + " critical");
		if (criticalCount > 0) {
			final Rectangle statusRectangleCritical = createStatusRectangleCritical(15);
			labelCriticalCount.setGraphic(statusRectangleCritical);
		} else {
			labelCriticalCount.setGraphic(null);
		}

		final int warningCount = messages.computeMessageCount(MessageType.WARNING);
		labelWarningCount.setText(warningCount + " warning");
		if (warningCount > 0) {
			final Rectangle statusRectangleWarning = createStatusRectangleWarning(15);
			labelWarningCount.setGraphic(statusRectangleWarning);
		} else {
			labelWarningCount.setGraphic(null);
		}

		final int infoCount = messages.computeMessageCount(MessageType.INFO);
		labelInfoCount.setText(infoCount + " info");
		if (infoCount > 0) {
			final Rectangle statusRectangleInfo = createStatusRectangleInfo(15);
			labelInfoCount.setGraphic(statusRectangleInfo);
		} else {
			labelInfoCount.setGraphic(null);
		}

		final UnfilteredTreeItem<TableRowDataMessage> unfilteredTreeItemRoot =
				customTreeTableView.getUnfilteredTreeItemRoot();
		unfilteredTreeItemRoot.getChildrenList().clear();

		final List<Message> messageList = messages.createDisplayMessageList();
		messageList.sort(Comparator.naturalOrder());

		fillTreeItems(unfilteredTreeItemRoot, messageList);

		customTreeTableView.setFilteredItems();
	}

	private static void fillTreeItems(
			final UnfilteredTreeItem<TableRowDataMessage> unfilteredTreeItemRoot,
			final List<Message> messageList) {

		String lastMessageCategory = null;
		UnfilteredTreeItem<TableRowDataMessage> unfilteredTreeItemCategory = null;
		for (final Message message : messageList) {

			final MessageType messageType = message.getMessageType();
			final String messageCategory = message.getMessageCategory();
			if (!messageCategory.equals(lastMessageCategory)) {

				final TableRowDataMessage tableRowDataMessageCategory =
						new TableRowDataMessage(true, messageType, messageCategory);
				unfilteredTreeItemCategory = new UnfilteredTreeItem<>(tableRowDataMessageCategory, true);
				unfilteredTreeItemRoot.getChildrenList().add(unfilteredTreeItemCategory);
				lastMessageCategory = messageCategory;
			}

			final String messageString = message.getMessageString();
			final TableRowDataMessage tableRowDataMessage =
					new TableRowDataMessage(false, messageType, messageString);
			final UnfilteredTreeItem<TableRowDataMessage> unfilteredTreeItemMessage =
					new UnfilteredTreeItem<>(tableRowDataMessage, true);
			unfilteredTreeItemCategory.getChildrenList().add(unfilteredTreeItemMessage);
		}
	}

	public static Rectangle createStatusRectangleOK(
			final int squareSideLength) {
		return createStatusRectangle(Color.LIGHTGREEN, squareSideLength);
	}

	public static Rectangle createStatusRectangleInfo(
			final int squareSideLength) {
		return createStatusRectangle(Color.LIGHTBLUE, squareSideLength);
	}

	public static Rectangle createStatusRectangleWarning(
			final int squareSideLength) {
		return createStatusRectangle(Color.YELLOW, squareSideLength);
	}

	public static Rectangle createStatusRectangleCritical(
			final int squareSideLength) {
		return createStatusRectangle(Color.RED, squareSideLength);
	}

	private static Rectangle createStatusRectangle(
			final Color color,
			final int squareSideLength) {

		final Rectangle rectangle = new Rectangle(squareSideLength, squareSideLength);
		rectangle.setFill(color);
		rectangle.setStroke(Color.BLACK);
		return rectangle;
	}
}
