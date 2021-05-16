package com.utils.gui_utils.objects.search_and_filter;

import com.utils.gui_utils.CustomControlAbstr;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.gui_utils.factories.LayoutControlsFactory;
import com.utils.gui_utils.help.StackPaneHelp;
import com.utils.gui_utils.help.patterns.VBoxHelpHtmlPatternGlob;
import com.utils.gui_utils.help.patterns.VBoxHelpHtmlPatternSimple;
import com.utils.gui_utils.help.patterns.VBoxHelpHtmlPatternUnixRegex;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

class HBoxRegexType extends CustomControlAbstr<HBox> {

	private final ToggleGroup toggleGroup;

	HBoxRegexType() {

		toggleGroup = new ToggleGroup();
	}

	@Override
	protected HBox createRoot() {

		final HBox hBoxRoot = LayoutControlsFactory.createHBox();

		final Label labelPatternType =
				BasicControlsFactory.createLabel("pattern type:", "bold");
		GuiUtils.addToHBox(hBoxRoot, labelPatternType,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		GuiUtils.addToHBox(hBoxRoot, new Region(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final ToggleButton toggleButtonSimple =
				BasicControlsFactory.createToggleButton("simple", null, toggleGroup);
		GuiUtils.addToHBox(hBoxRoot, toggleButtonSimple,
				Pos.CENTER, Priority.NEVER, 0, 0, 0, 7);

		final StackPaneHelp stackPaneHelpSimple = new StackPaneHelp(mouseEvent -> {
			if (GuiUtils.isLeftClick(mouseEvent)) {
				showHelpSimple();
			}
		});
		GuiUtils.addToHBox(hBoxRoot, stackPaneHelpSimple.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		GuiUtils.addToHBox(hBoxRoot, new Region(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final ToggleButton toggleButtonGlobRegex =
				BasicControlsFactory.createToggleButton("glob regex", null, toggleGroup);
		GuiUtils.addToHBox(hBoxRoot, toggleButtonGlobRegex,
				Pos.CENTER, Priority.NEVER, 0, 0, 0, 7);

		final StackPaneHelp stackPaneHelpGlob = new StackPaneHelp(mouseEvent -> {
			if (GuiUtils.isLeftClick(mouseEvent)) {
				showHelpGlob();
			}
		});
		GuiUtils.addToHBox(hBoxRoot, stackPaneHelpGlob.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		GuiUtils.addToHBox(hBoxRoot, new Region(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final ToggleButton toggleButtonUnixRegex =
				BasicControlsFactory.createToggleButton("unix regex", null, toggleGroup);
		GuiUtils.addToHBox(hBoxRoot, toggleButtonUnixRegex,
				Pos.CENTER, Priority.NEVER, 0, 0, 0, 7);

		final StackPaneHelp stackPaneHelpRegex = new StackPaneHelp(mouseEvent -> {
			if (GuiUtils.isLeftClick(mouseEvent)) {
				showHelpUnixRegex();
			}
		});
		GuiUtils.addToHBox(hBoxRoot, stackPaneHelpRegex.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		GuiUtils.addToHBox(hBoxRoot, new Region(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		toggleButtonSimple.setSelected(true);

		return hBoxRoot;
	}

	private void showHelpSimple() {
		new VBoxHelpHtmlPatternSimple().showWindow(getRoot().getScene());
	}

	private void showHelpGlob() {
		new VBoxHelpHtmlPatternGlob().showWindow(getRoot().getScene());
	}

	private void showHelpUnixRegex() {
		new VBoxHelpHtmlPatternUnixRegex().showWindow(getRoot().getScene());
	}

	int getPatternType() {

		int patternType = -1;
		final Toggle selectedToggle = toggleGroup.getSelectedToggle();
		if (selectedToggle != null) {
			patternType = toggleGroup.getToggles().indexOf(selectedToggle);
		}
		return patternType;
	}
}
