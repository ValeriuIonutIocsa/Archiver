package com.utils.io;

import java.io.File;

import org.junit.jupiter.api.Test;

class DesktopUtilsTest {

	@Test
	void testTryBrowse() throws Exception {

		final String filePathString = "C:\\Users\\Vevu\\Desktop\\nf\\test.mhtml";
		final String url = new File(filePathString).toURI().toURL().toExternalForm();

		DesktopUtils.tryBrowse(url);
	}

	@Test
	void testTryOpen() {

		final String filePathString = "C:\\Users\\Vevu\\Desktop\\nf\\test.mhtml";

		final File file = new File(filePathString);
		DesktopUtils.tryOpen(file);
	}
}
