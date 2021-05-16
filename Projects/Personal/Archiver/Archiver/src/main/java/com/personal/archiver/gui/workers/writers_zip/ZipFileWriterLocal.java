package com.personal.archiver.gui.workers.writers_zip;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.personal.archiver.gui.data.FileToArchive;
import com.utils.gui_utils.alerts.CustomAlertError;
import com.utils.gui_utils.alerts.CustomAlertException;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class ZipFileWriterLocal implements ZipFileWriter {

	private final Path workingDirPath;
	private final FileSystem zipFileSystem;
	private final Path zipFilePath;

	private final List<Path> failedToCopyFilePathList;

	ZipFileWriterLocal(
			final Path workingDirPath,
			final FileSystem zipFileSystem,
			final Path zipFilePath) {

		this.workingDirPath = workingDirPath;
		this.zipFileSystem = zipFileSystem;
		this.zipFilePath = zipFilePath;

		failedToCopyFilePathList = new ArrayList<>();
	}

	@Override
	public void copyFileToZip(
			final FileToArchive fileToArchive) {

		final Path filePath = fileToArchive.getFilePath();
		try {
			final String relativePathString = workingDirPath.relativize(filePath).toString();
			final Path zipPath = zipFileSystem.getPath("/", relativePathString);
			Files.copy(filePath, zipPath, StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.COPY_ATTRIBUTES);

		} catch (final Exception exc) {
			failedToCopyFilePathList.add(filePath);
			Logger.printError("failed to copy file to the archive:" +
					System.lineSeparator() + fileToArchive.getFilePath());
			Logger.printException(exc);
		}
	}

	@Override
	public void copyFolderToZip(
			final FileToArchive fileToArchive) {

		final Path filePath = fileToArchive.getFilePath();
		try {
			final String relativePathString = workingDirPath.relativize(filePath).toString();
			final Path zipPath = zipFileSystem.getPath("/", relativePathString);
			Files.createDirectory(zipPath);

		} catch (final Exception exc) {
			failedToCopyFilePathList.add(filePath);
			Logger.printError("failed to copy folder to the archive:" +
					System.lineSeparator() + fileToArchive.getFilePath());
			Logger.printException(exc);
		}
	}

	@Override
	public void closeZipFileSystem() {

		try {
			zipFileSystem.close();

		} catch (final Exception exc) {
			new CustomAlertException("error!",
					"failed to close the ZIP file system!", exc).showAndWait();
			Logger.printException(exc);
		}
	}

	@Override
	public void printErrors() {

		if (!failedToCopyFilePathList.isEmpty()) {

			final String message = "archive created but:" +
					System.lineSeparator() + "failed to copy " +
					failedToCopyFilePathList.size() + " files!";
			new CustomAlertError("error!", message).showAndWait();
		}
	}

	@Override
	public void deleteFiles() {
		FactoryFileDeleter.getInstance().deleteFile(zipFilePath, false);
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	Path getZipFilePath() {
		return zipFilePath;
	}
}
