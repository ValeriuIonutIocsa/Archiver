package com.personal.archiver.gui.workers.writers_zip;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.personal.archiver.gui.data.FileToArchive;
import com.utils.gui.alerts.CustomAlertError;
import com.utils.gui.alerts.CustomAlertException;
import com.utils.io.PathUtils;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class ZipFileWriterLocal implements ZipFileWriter {

	private final String workingDirPathString;
	private final FileSystem zipFileSystem;
	private final String zipFilePathString;

	private final List<String> failedToCopyFilePathStringList;

	ZipFileWriterLocal(
			final String workingDirPathString,
			final FileSystem zipFileSystem,
			final String zipFilePathString) {

		this.workingDirPathString = workingDirPathString;
		this.zipFileSystem = zipFileSystem;
		this.zipFilePathString = zipFilePathString;

		failedToCopyFilePathStringList = new ArrayList<>();
	}

	@Override
	public void copyFileToZip(
			final FileToArchive fileToArchive) {

		final String filePathString = fileToArchive.getFilePathString();
		try {
			final String relativePathString =
					PathUtils.computeRelativePath(workingDirPathString, filePathString);
			final Path zipPath = zipFileSystem.getPath("/", relativePathString);
			Files.copy(Paths.get(filePathString), zipPath, StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.COPY_ATTRIBUTES);

		} catch (final Exception exc) {
			failedToCopyFilePathStringList.add(filePathString);
			Logger.printError("failed to copy file to the archive:" +
					System.lineSeparator() + filePathString);
			Logger.printException(exc);
		}
	}

	@Override
	public void copyFolderToZip(
			final FileToArchive fileToArchive) {

		final String filePathString = fileToArchive.getFilePathString();
		try {
			final String relativePathString =
					PathUtils.computeRelativePath(workingDirPathString, filePathString);
			if (StringUtils.isNotBlank(relativePathString)) {

				final Path zipPath = zipFileSystem.getPath("/", relativePathString);
				Files.createDirectory(zipPath);
			}

		} catch (final Exception exc) {
			failedToCopyFilePathStringList.add(filePathString);
			Logger.printError("failed to copy folder to the archive:" +
					System.lineSeparator() + filePathString);
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

		if (!failedToCopyFilePathStringList.isEmpty()) {

			final String message = "archive created but:" +
					System.lineSeparator() + "failed to copy " +
					failedToCopyFilePathStringList.size() + " files!";
			new CustomAlertError("error!", message).showAndWait();
		}
	}

	@Override
	public void deleteFiles() {

		FactoryFileDeleter.getInstance().deleteFile(zipFilePathString, false, false);
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	String getZipFilePathString() {
		return zipFilePathString;
	}
}
