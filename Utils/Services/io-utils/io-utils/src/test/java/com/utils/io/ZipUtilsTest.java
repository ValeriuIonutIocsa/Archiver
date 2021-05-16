package com.utils.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class ZipUtilsTest {

	@Test
	void testCreateZip() {

		final String srcFolderPathString = "C:\\Users\\Vevu\\FileSynchronizer\\inputs";
		final String zipFilePathString = "C:\\Users\\Vevu\\FileSynchronizer\\tmp\\test.zip";
		final boolean useTempFile = false;

		final Path srcFolderPath = Paths.get(srcFolderPathString);
		final Path zipFilePath = Paths.get(zipFilePathString);
		final boolean success = ZipUtils.createZip(
				srcFolderPath, zipFilePath, useTempFile, true, 12, true, true);
		assertTrue(success);
	}

	@Test
	void testExtractZip() {

		final String zipFilePathString = "C:\\Users\\Vevu\\FileSynchronizer\\tmp\\test.zip";
		final String dstFolderPathString = "C:\\Users\\Vevu\\FileSynchronizer\\tmp\\test_extracted";
		final boolean useTempFile = false;

		final Path zipFilePath = Paths.get(zipFilePathString);
		final Path dstFolderPath = Paths.get(dstFolderPathString);
		final boolean success = ZipUtils.extractZip(
				zipFilePath, dstFolderPath, useTempFile, true, 12, true, true);
		assertTrue(success);
	}
}
