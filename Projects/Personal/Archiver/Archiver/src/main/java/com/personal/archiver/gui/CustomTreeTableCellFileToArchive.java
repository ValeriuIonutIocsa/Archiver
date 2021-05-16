package com.personal.archiver.gui;

import java.nio.file.Path;
import java.util.List;

import com.personal.archiver.gui.data.FileToArchive;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.gui_utils.factories.LayoutControlsFactory;
import com.utils.gui_utils.icons.FileSystemIconRetriever;
import com.utils.gui_utils.objects.tables.tree_table.CustomTreeTableCell;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

class CustomTreeTableCellFileToArchive extends CustomTreeTableCell<FileToArchive, Object> {

	private final VBoxArchiver vBoxArchiver;

	CustomTreeTableCellFileToArchive(
			final VBoxArchiver vBoxArchiver) {

		this.vBoxArchiver = vBoxArchiver;
	}

	@Override
	public void setText(
			final StackPane stackPane,
			final Object item) {

		final FileToArchive fileToArchive = getRowData();
		if (fileToArchive != null) {

			final HBox hBox = LayoutControlsFactory.createHBox();

			final CheckBox checkBox = BasicControlsFactory.createCheckBox("");
			final boolean selected = fileToArchive.isSelected();
			checkBox.setSelected(selected);

			checkBox.selectedProperty().addListener((
					observable,
					oldValue,
					newValue) -> {

				if (newValue) {
					vBoxArchiver.createNewOutputPath(fileToArchive);
				}

				setChildrenSelectedRec(fileToArchive, newValue);

				final TreeTableView<FileToArchive> treeTableView = getTreeTableView();
				treeTableView.refresh();

				vBoxArchiver.getRoot().getScene().getRoot().requestFocus();
			});
			GuiUtils.addToHBox(hBox, checkBox,
					Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

			final Label label = new Label(item.toString());
			final Node labelGraphic = createLabelGraphic(item);
			label.setGraphic(labelGraphic);
			GuiUtils.addToHBox(hBox, label,
					Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

			final Pos textAlignment = getTextAlignmentValue();
			GuiUtils.addToStackPane(stackPane, hBox, textAlignment, 1, 1, 1, 1);
		}
	}

	private static void setChildrenSelectedRec(
			final FileToArchive fileToArchive,
			final boolean selected) {

		fileToArchive.setSelected(selected);

		final List<FileToArchive> childrenList = fileToArchive.getChildrenList();
		if (childrenList != null) {

			for (final FileToArchive childFileToArchive : childrenList) {
				setChildrenSelectedRec(childFileToArchive, selected);
			}
		}
	}

	@Override
	public Node createLabelGraphic(
			final Object item) {

		Node labelGraphic = null;
		final FileToArchive fileToArchive = getRowData();
		if (fileToArchive != null) {

			final Path filePath = fileToArchive.getFilePath();
			final Image image = new FileSystemIconRetriever(filePath).work();
			labelGraphic = new ImageView(image);
		}
		return labelGraphic;
	}

	@Override
	public ContextMenu createContextMenu(
			final Object item) {

		ContextMenu contextMenu = null;
		final FileToArchive fileToArchive = getRowData();
		if (fileToArchive != null) {

			final String fileName = fileToArchive.getFileName();
			if (fileName.endsWith(".zip")) {

				contextMenu = new ContextMenu();

				final MenuItem menuItemExtractHere = new MenuItem("extract here");
				menuItemExtractHere.setOnAction(event -> vBoxArchiver.extractHere(fileToArchive));
				contextMenu.getItems().add(menuItemExtractHere);
			}
		}
		return contextMenu;
	}
}
