package com.utils.gui_utils.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTestVitesco;
import com.utils.log.Logger;

class CustomTextInputDialogTest extends AbstractCustomApplicationTestVitesco {

	CustomTextInputDialogTest() {
		super(null);
	}

	@Test
	void testShow() {

		final CustomTextInputDialog customTextInputDialog =
				new CustomTextInputDialog("Title", "message", "default value");
		customTextInputDialog.showAndWait();

		final String result = customTextInputDialog.getResult();
		Logger.printLine("result: " + result);
	}
}
