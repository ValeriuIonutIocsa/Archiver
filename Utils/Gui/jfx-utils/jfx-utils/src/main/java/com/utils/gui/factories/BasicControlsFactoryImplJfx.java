package com.utils.gui.factories;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.JFXToggleNode;
import com.utils.annotations.ApiMethod;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;

public class BasicControlsFactoryImplJfx extends BasicControlsFactoryImpl {

	@Override
	@ApiMethod
	public Button createButton(
			final String text,
			final String... styleClassElements) {

		final Button button = new JFXButton(text);
		button.setMnemonicParsing(false);
		if (styleClassElements != null) {
			button.getStyleClass().addAll(styleClassElements);
		}
		button.setMinWidth(Region.USE_PREF_SIZE);
		button.setMinHeight(Region.USE_PREF_SIZE);
		return button;
	}

	@Override
	@ApiMethod
	public CheckBox createCheckBox(
			final String text,
			final String... styleClassElements) {

		final CheckBox checkBox = new JFXCheckBox(text);
		checkBox.setMnemonicParsing(false);
		if (styleClassElements != null) {
			checkBox.getStyleClass().addAll(styleClassElements);
		}
		checkBox.setMinWidth(Region.USE_PREF_SIZE);
		checkBox.setMinHeight(Region.USE_PREF_SIZE);
		return checkBox;
	}

	@Override
	@ApiMethod
	public ToggleButton createToggleButton(
			final String text,
			final String[] styleClassElements,
			final ToggleGroup toggleGroup) {

		final ToggleButton toggleButton = new JFXToggleButton();
		toggleButton.setMnemonicParsing(false);
		toggleButton.setText(text);
		if (styleClassElements != null) {
			toggleButton.getStyleClass().addAll(styleClassElements);
		}
		if (toggleGroup != null) {
			toggleButton.setToggleGroup(toggleGroup);
		}
		toggleButton.setMaxHeight(20);
		toggleButton.setPadding(new Insets(0));
		return toggleButton;
	}

	@Override
	@ApiMethod
	public ToggleButton createToggleNode(
			final String text,
			final String... styleClassElements) {

		final Label label = createLabel(text, styleClassElements);
		return new JFXToggleNode(label);
	}
}
