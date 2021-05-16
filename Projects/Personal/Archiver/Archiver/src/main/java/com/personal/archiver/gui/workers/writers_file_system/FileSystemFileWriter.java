package com.personal.archiver.gui.workers.writers_file_system;

import java.nio.file.Path;

public interface FileSystemFileWriter {

	void createDirectory(
			Path path);

	void copyFile(
			Path zipPath,
			Path fileSystemPath,
			boolean destFileExists);

	void deleteFolder(
			Path path);

	void deleteFile(
			Path path);

	void printErrors();
}
