package com.utils.gui_utils.help;

import com.utils.gui_utils.CustomControlAbstr;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.LayoutControlsFactory;
import com.utils.gui_utils.icons.ImagesGuiUtils;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class StackPaneHelp extends CustomControlAbstr<StackPane> {

	private final EventHandler<? super MouseEvent> eventHandler;

	public StackPaneHelp(
			final EventHandler<? super MouseEvent> eventHandler) {

		this.eventHandler = eventHandler;
	}

	@Override
	protected StackPane createRoot() {

		final StackPane stackPaneRoot =
				LayoutControlsFactory.createStackPane(Pos.CENTER_LEFT);
		stackPaneRoot.getStylesheets().add("com/utils/gui_utils/help/stack_pane_help.css");
		stackPaneRoot.getStyleClass().add("hoverOn");
		stackPaneRoot.setMaxWidth(Double.NEGATIVE_INFINITY);
		stackPaneRoot.setMaxHeight(Double.NEGATIVE_INFINITY);
		stackPaneRoot.setMinWidth(Double.NEGATIVE_INFINITY);
		stackPaneRoot.setMinHeight(Double.NEGATIVE_INFINITY);
		stackPaneRoot.setPrefWidth(-1);
		stackPaneRoot.setPrefHeight(-1);

		final Image imageHelp = ImagesGuiUtils.createImageHelp();
		final ImageView imageViewHelp = new ImageView(imageHelp);
		GuiUtils.addToStackPane(stackPaneRoot, imageViewHelp,
				Pos.CENTER_LEFT, 3, 3, 3, 3);

		stackPaneRoot.setOnMouseClicked(eventHandler);

		return stackPaneRoot;
	}
}
