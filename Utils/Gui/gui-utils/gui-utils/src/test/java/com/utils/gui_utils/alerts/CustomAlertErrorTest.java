package com.utils.gui_utils.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTestVitesco;

class CustomAlertErrorTest extends AbstractCustomApplicationTestVitesco {

	CustomAlertErrorTest() {
		super(null);
	}

	@Test
	void testShow() {

		final CustomAlertError customAlertError =
				new CustomAlertError("Title", "message");
		customAlertError.showAndWait();
	}
}
