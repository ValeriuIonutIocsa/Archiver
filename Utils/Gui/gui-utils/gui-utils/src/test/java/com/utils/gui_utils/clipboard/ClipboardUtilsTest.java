package com.utils.gui_utils.clipboard;

import org.junit.jupiter.api.Test;

class ClipboardUtilsTest {

	@Test
	void testPutStringInClipBoard() {

		final String string;
		final int input = Integer.parseInt("11");
		if (input == 1) {
			string = "Single line string.";
		} else if (input == 2) {
			string = "Single line string\twith\tsome tabs.";

		} else if (input == 11) {
			string = "^.*A{0,3}.*$";

		} else if (input == 101) {
			string = "Multi line string" + System.lineSeparator() +
					"first \t line" + "\n" +
					"second line" + System.lineSeparator() +
					"third\tline.";

		} else {
			string = "";
		}

		ClipboardUtils.putStringInClipBoard(string);
	}
}
