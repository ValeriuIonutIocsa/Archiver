package com.utils.gui_utils.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTestVitesco;

class CustomAlertInfoTest extends AbstractCustomApplicationTestVitesco {

	CustomAlertInfoTest() {
		super(null);
	}

	@Test
	void testShow() {

		final CustomAlertInfo customAlertInfo =
				new CustomAlertInfo("Title", "message");
		customAlertInfo.showAndWait();
	}
}
