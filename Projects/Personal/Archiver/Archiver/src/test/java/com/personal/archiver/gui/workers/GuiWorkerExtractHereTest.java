package com.personal.archiver.gui.workers;

import org.junit.jupiter.api.Test;

import com.personal.archiver.gui.data.FileToArchive;
import com.utils.gui.GuiUtils;
import com.utils.test.TestInputUtils;

class GuiWorkerExtractHereTest {

	@Test
	void testWork() throws Exception {

		final String zipFilePathString;
		final int input = TestInputUtils.parseTestInputNumber("1");
		if (input == 1) {
			zipFilePathString = "D:\\p\\bm\\g12\\905\\BMG12_0U0_905.zip";
		} else if (input == 2) {
			zipFilePathString = "D:\\p\\0g\\0a3\\911\\0g0a3_0u0_911.zip";
		} else if (input == 3) {
			zipFilePathString = "D:\\test_archiver\\ChronaValidatorInputGenerator.zip";
		} else {
			throw new RuntimeException();
		}

		final FileToArchive fileToArchive = new FileToArchive(null, zipFilePathString, false);

		GuiUtils.startPlatform();
		final GuiWorkerExtractHere guiWorkerExtractHere =
				new GuiWorkerExtractHere(null, null, fileToArchive, false);
		guiWorkerExtractHere.start();

		guiWorkerExtractHere.join();
		GuiUtils.stopPlatform();
	}
}
