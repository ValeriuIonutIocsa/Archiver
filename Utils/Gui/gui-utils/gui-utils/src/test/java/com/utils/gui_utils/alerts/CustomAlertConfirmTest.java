package com.utils.gui_utils.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTestVitesco;
import com.utils.log.Logger;

import javafx.scene.control.ButtonType;

class CustomAlertConfirmTest extends AbstractCustomApplicationTestVitesco {

	CustomAlertConfirmTest() {
		super(null);
	}

	@Test
	void testShow() {

		final CustomAlertConfirm customAlertConfirm =
				new CustomAlertConfirm("Title", "message", ButtonType.NO, ButtonType.YES);
		customAlertConfirm.showAndWait();

		final ButtonType result = customAlertConfirm.getResult();
		Logger.printLine("result: " + result);
	}
}
