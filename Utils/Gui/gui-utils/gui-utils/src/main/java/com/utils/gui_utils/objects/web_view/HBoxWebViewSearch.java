package com.utils.gui_utils.objects.web_view;

import com.utils.gui_utils.CustomControlAbstr;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.gui_utils.factories.LayoutControlsFactory;
import com.utils.gui_utils.icons.ImagesGuiUtils;
import com.utils.string.StrUtils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class HBoxWebViewSearch extends CustomControlAbstr<HBox> {

	private static final String SEARCH_TOOLTIP_TEXT = "enter some text and click enter" +
			System.lineSeparator() + "to search in the console below";

	protected final CustomWebView customWebView;

	public HBoxWebViewSearch(
			final CustomWebView customWebView) {

		this.customWebView = customWebView;
	}

	@Override
	protected HBox createRoot() {

		final HBox hBoxRoot = LayoutControlsFactory.createHBox();
		hBoxRoot.setMinWidth(0);

		createComponents(hBoxRoot);

		return hBoxRoot;
	}

	protected void createComponents(
			final HBox hBoxRoot) {

		final Label labelSearch = BasicControlsFactory.createLabel("search:");
		labelSearch.setTooltip(BasicControlsFactory.createTooltip(SEARCH_TOOLTIP_TEXT));
		GuiUtils.addToHBox(hBoxRoot, labelSearch,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 5);

		final ToggleButton caseSensitiveToggleButton = createCaseSensitiveToggleButton();

		final TextField textFieldSearch = BasicControlsFactory.createTextField("");
		textFieldSearch.setTooltip(BasicControlsFactory.createTooltip(SEARCH_TOOLTIP_TEXT));
		textFieldSearch.setOnAction(event -> customWebView.search(
				textFieldSearch.getText(), caseSensitiveToggleButton.isSelected()));
		customWebView.getRoot().setOnKeyPressed(event -> {
			if (event.isControlDown() && event.getCode() == KeyCode.F) {
				textFieldSearch.requestFocus();
			}
		});
		GuiUtils.addToHBox(hBoxRoot, textFieldSearch,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 5);

		final ImageView imageViewSearch = new ImageView(ImagesGuiUtils.createImageSearch());
		final StackPane stackPaneImageViewSearch = GuiUtils.addToHBox(hBoxRoot, imageViewSearch,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 5);
		stackPaneImageViewSearch.setPadding(new Insets(0, 2, 0, 2));
		stackPaneImageViewSearch.setOnMouseClicked(mouseEvent -> {
			if (GuiUtils.isLeftClick(mouseEvent)) {
				customWebView.search(textFieldSearch.getText(), caseSensitiveToggleButton.isSelected());
			}
		});
		GuiUtils.setDefaultBorder(stackPaneImageViewSearch);

		GuiUtils.addToHBox(hBoxRoot, caseSensitiveToggleButton,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 5);
	}

	private static ToggleButton createCaseSensitiveToggleButton() {

		final ToggleButton caseSensitiveToggleButton =
				BasicControlsFactory.createToggleNode("Aa", "bold");
		caseSensitiveToggleButton.setTooltip(
				BasicControlsFactory.createTooltip("case sensitive search"));
		GuiUtils.setDefaultBorder(caseSensitiveToggleButton);
		return caseSensitiveToggleButton;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
