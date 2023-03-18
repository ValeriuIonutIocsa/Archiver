package com.utils.gui.styles.vitesco.themes;

public final class FactoryGuiTheme {

	private static final GuiTheme[] VALUES = GuiTheme.values();

	private FactoryGuiTheme() {
	}

	public static GuiTheme computeInstance(
			final String nameParam) {

		GuiTheme guiTheme = GuiTheme.VITESCO;
		for (final GuiTheme value : VALUES) {

			final String name = value.name();
			if (name.equals(nameParam)) {
				guiTheme = value;
				break;
			}
		}
		return guiTheme;
	}
}
