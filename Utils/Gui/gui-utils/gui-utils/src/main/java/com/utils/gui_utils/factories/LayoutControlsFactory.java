package com.utils.gui_utils.factories;

import com.utils.annotations.ApiMethod;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public final class LayoutControlsFactory {

	private LayoutControlsFactory() {
	}

	@ApiMethod
	public static Pane createPane() {
		return createPane(true);
	}

	@ApiMethod
	public static Pane createPane(
			final boolean emptyBackground) {

		final Pane pane = new Pane();
		pane.setBackground(Background.EMPTY);
		return pane;
	}

	@ApiMethod
	public static HBox createHBox() {
		return createHBox(true);
	}

	@ApiMethod
	public static HBox createHBox(
			final boolean emptyBackground) {

		final HBox hBox = new HBox();
		if (emptyBackground) {
			hBox.setBackground(Background.EMPTY);
		}
		return hBox;
	}

	@ApiMethod
	public static VBox createVBox() {
		return createVBox(true);
	}

	@ApiMethod
	public static VBox createVBox(
			final boolean emptyBackground) {

		final VBox vBox = new VBox();
		if (emptyBackground) {
			vBox.setBackground(Background.EMPTY);
		}
		return vBox;
	}

	@ApiMethod
	public static GridPane createGridPane() {
		return createGridPane(true);
	}

	@ApiMethod
	public static GridPane createGridPane(
			final boolean emptyBackground) {

		final GridPane gridPane = new GridPane();
		if (emptyBackground) {
			gridPane.setBackground(Background.EMPTY);
		}
		return gridPane;
	}

	/**
	 * @param ratio
	 *            a double value between 0 and 1
	 * @return a ColumnConstraints object to be added to a GridPane
	 */
	@ApiMethod
	public static ColumnConstraints createPercentageConstraints(
			final double ratio) {

		final ColumnConstraints columnConstraints = new ColumnConstraints();
		final double percent = ratio * 100.0;
		columnConstraints.setPercentWidth(percent);
		return columnConstraints;
	}

	@ApiMethod
	public static StackPane createStackPane(
			final Pos pos,
			final Node... nodes) {

		final StackPane stackPane = new StackPane(nodes);
		stackPane.setBackground(Background.EMPTY);
		stackPane.setAlignment(pos);
		return stackPane;
	}

	@ApiMethod
	public static FlowPane createFlowPane() {
		return createFlowPane(true);
	}

	@ApiMethod
	public static FlowPane createFlowPane(
			final boolean emptyBackground) {

		final FlowPane flowPane = new FlowPane();
		if (emptyBackground) {
			flowPane.setBackground(Background.EMPTY);
		}
		return flowPane;
	}

	@ApiMethod
	public static TilePane createTilePane() {
		return createTilePane(true);
	}

	@ApiMethod
	public static TilePane createTilePane(
			final boolean emptyBackground) {

		final TilePane tilePane = new TilePane();
		if (emptyBackground) {
			tilePane.setBackground(Background.EMPTY);
		}
		return tilePane;
	}

	@ApiMethod
	public static ScrollPane createScrollPane(
			final Node content) {

		final ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		if (content != null) {
			scrollPane.setContent(content);
		}
		return scrollPane;
	}

	@ApiMethod
	public static TitledPane createTitledPane(
			final String title,
			final String... styleClassElements) {

		final TitledPane titledPane = new TitledPane();
		if (styleClassElements != null) {
			titledPane.getStyleClass().addAll(styleClassElements);
		}
		titledPane.setText(title);
		return titledPane;
	}

	@ApiMethod
	public static TabPane createTabPaneNoHeaders() {

		final TabPane tabPane = createTabPane();
		tabPane.getStylesheets().add("com/utils/gui_utils/factories/tab_pane_no_headers.css");
		tabPane.getStyleClass().add("tab-pane-no-headers");
		return tabPane;
	}

	@ApiMethod
	public static TabPane createTabPane() {

		final TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		return tabPane;
	}
}
