package com.personal.archiver.gui.workers.writers_file_system;

import java.nio.file.Path;

public interface FileSystemFileWriter {

	void createDirectory(
            String pathString);

	void copyFile(
			Path zipPath,
			String fileSystemPathString,
			boolean dstFileExists);

	void deleteFolder(
			String pathString);

	void deleteFile(
			String pathString);

	void printErrors();
}
