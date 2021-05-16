package com.personal.archiver.gui.workers;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.personal.archiver.gui.data.FileToArchive;
import com.utils.gui_utils.GuiUtils;

class GuiWorkerExtractHereTest {

	@Test
	void testWork() throws Exception {

		final String zipFilePathString;
		final int input = Integer.parseInt("1");
		if (input == 1) {
			zipFilePathString = "D:\\p\\bm\\g12\\905\\BMG12_0U0_905.zip";
		} else if (input == 2) {
			zipFilePathString = "D:\\p\\0g\\0a3\\911\\0g0a3_0u0_911.zip";
		} else if (input == 3) {
			zipFilePathString = "D:\\test_archiver\\ChronaValidatorInputGenerator.zip";
		} else {
			throw new RuntimeException();
		}

		final Path zipFilePath = Paths.get(zipFilePathString);
		final FileToArchive fileToArchive = new FileToArchive(null, zipFilePath, false);

		GuiUtils.startPlatform();
		final GuiWorkerExtractHere guiWorkerExtractHere =
				new GuiWorkerExtractHere(null, null, fileToArchive, null);
		guiWorkerExtractHere.start();

		guiWorkerExtractHere.join();
		GuiUtils.stopPlatform();
	}
}
