package com.utils.gui.objects.tables.table_view;

import com.utils.gui.GuiUtils;
import com.utils.gui.objects.LevelGauge;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public abstract class AbstractCustomTableCellPercentage<
		RowDataT,
		CellDataT> extends CustomTableCell<RowDataT, CellDataT> {

	private final String tempFolderPathString;
	private final Color fullSpaceColor;
	private final Color emptySpaceColor;

	protected AbstractCustomTableCellPercentage(
			final String tempFolderPathString,
			final Color fullSpaceColor,
			final Color emptySpaceColor) {

		this.tempFolderPathString = tempFolderPathString;
		this.fullSpaceColor = fullSpaceColor;
		this.emptySpaceColor = emptySpaceColor;
	}

	@Override
	public void setText(
			final StackPane stackPane,
			final Object item) {

		final Node graphic = createGraphic();
		GuiUtils.addToStackPane(stackPane, graphic, Pos.CENTER, 0, 0, 0, 0);
	}

	private Node createGraphic() {

		Node graphic = null;
		final RowDataT rowData = getRowData();
		if (rowData != null) {

			final double cpuLoadPercentage = computePercentage(rowData);
			final double level = cpuLoadPercentage / 100.0;
			final boolean validLevel = level <= 1;
			final LevelGauge levelGauge = new LevelGauge(tempFolderPathString,
					level, 2, validLevel, fullSpaceColor, emptySpaceColor);
			graphic = levelGauge.getRoot();
		}
		return graphic;
	}

	protected abstract double computePercentage(
			RowDataT rowData);
}
