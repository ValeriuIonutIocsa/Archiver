package com.utils.gui.styles.vitesco.themes;

public enum GuiTheme {

	VITESCO("Vitesco Technologies",
			"com/utils/gui/styles/vitesco/style_theme_vitesco.css",
			"Vitesco Technologies Theme: a theme in tone with the \"Vitesco Technologies\" theme"),
	STANDARD("Standard",
			"com/utils/gui/styles/vitesco/style_theme_standard.css",
			"Standard Theme: the standard theme of the application"),
	DARK("Dark",
			"com/utils/gui/styles/vitesco/style_theme_dark.css",
			"Dark Theme: a dark theme (favours dark grey and black instead of light grey and white)");

	private final String displayName;
	private final String styleSheetResourcePathString;
	private final String description;

	GuiTheme(
			final String displayName,
			final String styleSheetResourcePathString,
			final String description) {

		this.displayName = displayName;
		this.styleSheetResourcePathString = styleSheetResourcePathString;
		this.description = description;
	}

	@Override
	public String toString() {
		return displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getStyleSheetResourcePathString() {
		return styleSheetResourcePathString;
	}

	public String getDescription() {
		return description;
	}
}
