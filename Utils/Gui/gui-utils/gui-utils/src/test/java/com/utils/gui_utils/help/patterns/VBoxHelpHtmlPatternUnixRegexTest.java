package com.utils.gui_utils.help.patterns;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTestVitesco;
import com.utils.gui_utils.GuiUtils;

import javafx.scene.layout.StackPane;

class VBoxHelpHtmlPatternUnixRegexTest extends AbstractCustomApplicationTestVitesco {

	VBoxHelpHtmlPatternUnixRegexTest() {
		super(null);
	}

	@Test
	void testLayout() {

		GuiUtils.runAndWait(() -> {

			final StackPane stackPaneContainer = getStackPaneContainer();
			new VBoxHelpHtmlPatternUnixRegex().showWindow(stackPaneContainer.getScene());
		});
	}
}
