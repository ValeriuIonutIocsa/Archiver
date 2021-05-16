package com.utils.gui_utils.objects.tables.table_view;

import com.utils.data_types.table.TableRowData;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.icons.ImagesGuiUtils;
import com.utils.gui_utils.objects.HBoxTitle;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;

public class HBoxTitleTableView<
		TableRowDataT extends TableRowData> extends HBoxTitle {

	public interface Thunk {

		void apply();
	}

	public HBoxTitleTableView(
			final String title,
			final String titleLabelId,
			final double titleMinWidth,
			final CustomTableView<TableRowDataT> customTableView,
			final TableItemIndexUpdater<TableRowDataT> tableItemIndexUpdater,
			final Thunk saveData) {

		super(title, titleLabelId, titleMinWidth);

		final ImageView imageViewUpArrow = new ImageView(ImagesGuiUtils.createImageUpArrow());
		imageViewUpArrow.setOnMouseClicked(mouseEvent -> {

			if (GuiUtils.isLeftClick(mouseEvent)) {
				customTableView.moveSelectedItem(-1, tableItemIndexUpdater);
				if (saveData != null) {
					saveData.apply();
				}
			}
		});
		GuiUtils.addToHBox(getRoot(), imageViewUpArrow,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		final ImageView imageViewDownArrow = new ImageView(ImagesGuiUtils.createImageDownArrow());
		imageViewDownArrow.setOnMouseClicked(mouseEvent -> {
			if (GuiUtils.isLeftClick(mouseEvent)) {
				customTableView.moveSelectedItem(1, tableItemIndexUpdater);
				if (saveData != null) {
					saveData.apply();
				}
			}
		});
		GuiUtils.addToHBox(getRoot(), imageViewDownArrow,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);
	}
}
