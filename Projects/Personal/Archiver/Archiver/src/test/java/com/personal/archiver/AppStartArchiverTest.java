package com.personal.archiver;

import org.junit.jupiter.api.Test;

class AppStartArchiverTest {

	@Test
	void testMain() {

		final String folderPathString;
		final String outputPathString;
		final int input = Integer.parseInt("1");
		if (input == 1) {
			folderPathString = "D:\\p\\0g\\0a3\\911\\0g0a3_0u0_911";
			outputPathString = "D:\\p\\0g\\0a3\\911\\0g0a3_0u0_911.zip";
		} else if (input == 2) {
			folderPathString = "C:\\Users\\Vevu\\Desktop\\nf\\pics";
			outputPathString = "C:\\Users\\Vevu\\Desktop\\nf\\pics.zip";
		} else {
			throw new RuntimeException();
		}

		final String[] args = new String[] {
				folderPathString,
				outputPathString
		};
		AppStartArchiver.main(args);
	}
}
