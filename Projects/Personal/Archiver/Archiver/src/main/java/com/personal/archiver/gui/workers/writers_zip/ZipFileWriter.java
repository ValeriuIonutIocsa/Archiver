package com.personal.archiver.gui.workers.writers_zip;

import com.personal.archiver.gui.data.FileToArchive;

public interface ZipFileWriter {

	void copyFileToZip(
			FileToArchive fileToArchive);

	void copyFolderToZip(
			FileToArchive fileToArchive);

	void closeZipFileSystem();

	void printErrors();

	void deleteFiles();
}
