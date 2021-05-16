package com.utils.gui_utils.logger;

import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.gui_utils.objects.web_view.CustomWebView;
import com.utils.gui_utils.objects.web_view.HBoxWebViewSearch;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class HBoxWebViewSearchLogger extends HBoxWebViewSearch {

	private final String title;

	public HBoxWebViewSearchLogger(
			final CustomWebView customWebView,
			final String title) {

		super(customWebView);

		this.title = title;
	}

	@Override
	protected void createComponents(
			final HBox hBoxRoot) {

		final Label labelTitle = BasicControlsFactory
				.createLabel(title, "boldFontSize10");
		labelTitle.setMinWidth(84);
		GuiUtils.addToHBox(hBoxRoot, labelTitle,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 5, 0, 5);

		final Separator separator = BasicControlsFactory.createSeparator(Orientation.VERTICAL);
		GuiUtils.addToHBox(hBoxRoot, separator,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		super.createComponents(hBoxRoot);

		final Button buttonClear = BasicControlsFactory.createButton("clear");
		buttonClear.setTooltip(BasicControlsFactory.createTooltip("clear the console below"));
		buttonClear.setOnAction(event -> customWebView.clear());
		GuiUtils.addToHBox(hBoxRoot, buttonClear,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 5, 0, 12);
	}
}
