package com.utils.gui_utils.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTestVitesco;

class CustomAlertWarningTest extends AbstractCustomApplicationTestVitesco {

	CustomAlertWarningTest() {
		super(null);
	}

	@Test
	void testShow() {

		final CustomAlertWarning customAlertWarning =
				new CustomAlertWarning("Title", "message");
		customAlertWarning.showAndWait();
	}
}
