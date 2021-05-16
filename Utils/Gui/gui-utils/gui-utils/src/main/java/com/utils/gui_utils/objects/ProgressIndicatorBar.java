package com.utils.gui_utils.objects;

import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.string.StrUtils;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

public class ProgressIndicatorBar extends StackPane {

	private final SimpleDoubleProperty value;

	public ProgressIndicatorBar() {

		value = new SimpleDoubleProperty();

		getStylesheets().add("com/utils/gui_utils/objects/progress_indicator_bar.css");

		setPrefWidth(150);
		setPrefHeight(24);
		setMinHeight(24);

		final Label label =
				BasicControlsFactory.createLabel("loading", "label-progress-indicator-bar");
		final SimpleStringProperty labelValue = new SimpleStringProperty();
		Bindings.bindBidirectional(labelValue, value, new StringConverter<>() {

			@Override
			public String toString(
					final Number number) {

				final String str;
				if (number == null) {
					str = "";

				} else {
					final double n = (double) number;
					if (n <= 0 || Double.isNaN(n)) {
						str = "";
					} else {
						str = StrUtils.doubleToPercentageString(n, 2);
					}
				}
				return str;
			}

			@Override
			public Number fromString(
					final String string) {
				return null;
			}
		});
		label.textProperty().bind(labelValue);

		final ProgressBar progressBar = new ProgressBar();
		progressBar.setPrefHeight(24);
		progressBar.setMinHeight(24);
		progressBar.setPrefWidth(Double.MAX_VALUE);
		progressBar.progressProperty().bind(value);

		getChildren().setAll(progressBar, label);
	}

	public void updateValue(
			final int count,
			final int total) {

		final double value = (double) count / total;
		updateValue(value);
	}

	public void updateValue(
			final double value) {
		GuiUtils.run(() -> this.value.setValue(value));
	}
}
