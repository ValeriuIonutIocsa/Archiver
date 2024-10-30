package com.personal.archiver;

import org.junit.jupiter.api.Test;

import com.utils.test.TestInputUtils;

class AppStartArchiverTest {

	@Test
	void testMain() {

		final String[] args;
		final int input = TestInputUtils.parseTestInputNumber("2");
		if (input == 1) {
			args = new String[] {};

		} else if (input == 2) {

			final String folderPathString;
			final String outputPathString;
			final int inputFilePaths = TestInputUtils.parseTestInputNumber("1");
			if (inputFilePaths == 1) {
				folderPathString = "D:\\p\\0g\\0a3\\911\\0g0a3_0u0_911";
				outputPathString = "D:\\p\\0g\\0a3\\911\\0g0a3_0u0_911.zip";
			} else if (inputFilePaths == 2) {
				folderPathString = "C:\\Users\\Vevu\\Desktop\\nf\\pics";
				outputPathString = "C:\\Users\\Vevu\\Desktop\\nf\\pics.zip";
			} else {
				throw new RuntimeException();
			}
			args = new String[] {
					folderPathString,
					outputPathString
			};

		} else {
			throw new RuntimeException();
		}

		AppStartArchiver.main(args);
	}
}
