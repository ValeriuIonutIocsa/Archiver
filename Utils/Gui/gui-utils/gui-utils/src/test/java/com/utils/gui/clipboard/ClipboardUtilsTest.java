package com.utils.gui.clipboard;

import org.junit.jupiter.api.Test;

import com.utils.concurrency.ThreadUtils;
import com.utils.gui.AbstractCustomApplicationTest;

class ClipboardUtilsTest extends AbstractCustomApplicationTest {

	@Test
	void testPutStringInClipBoard() {

		final String string;
		final int input = Integer.parseInt("101");
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
		ThreadUtils.trySleep(1_000);
	}
}
