package com.utils.gui_utils.help.patterns;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTestVitesco;
import com.utils.gui_utils.GuiUtils;

import javafx.scene.layout.StackPane;

class VBoxHelpHtmlPatternGlobTest extends AbstractCustomApplicationTestVitesco {

	VBoxHelpHtmlPatternGlobTest() {
		super(null);
	}

	@Test
	void testLayout() {

		GuiUtils.runAndWait(() -> {

			final StackPane stackPaneContainer = getStackPaneContainer();
			new VBoxHelpHtmlPatternGlob().showWindow(stackPaneContainer.getScene());
		});
	}
}
