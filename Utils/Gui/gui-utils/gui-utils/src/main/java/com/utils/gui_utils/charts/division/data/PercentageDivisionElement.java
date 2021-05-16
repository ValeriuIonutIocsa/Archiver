package com.utils.gui_utils.charts.division.data;

import javafx.scene.paint.Color;

public class PercentageDivisionElement {

	private final String name;
	private final double percentage;
	private final Color color;

	public PercentageDivisionElement(
			final String name,
			final double percentage,
			final Color color) {

		this.name = name;
		this.percentage = percentage;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public double getPercentage() {
		return percentage;
	}

	public Color getColor() {
		return color;
	}
}
