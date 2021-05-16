package com.utils.gui_utils.alerts;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTestVitesco;

class CustomAlertExceptionTest extends AbstractCustomApplicationTestVitesco {

	CustomAlertExceptionTest() {
		super(null);
	}

	@Test
	void testShow() {

		final CustomAlertException customAlertException =
				new CustomAlertException("Title", "message", new Exception());
		customAlertException.showAndWait();
	}
}
