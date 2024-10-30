package com.personal.archiver.gui.workers.writers_file_system;

import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.utils.gui.alerts.CustomAlertError;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.folder_deleters.FactoryFolderDeleter;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;

public class FileSystemFileWriterImpl implements FileSystemFileWriter {

	private final List<String> failedToCreateDirectoryPathList;
	private final List<String> failedToCopyFilePathList;
	private final List<String> failedToDeleteDirectoryPathList;
	private final List<String> failedToDeleteFilePathList;

	public FileSystemFileWriterImpl() {

		failedToCreateDirectoryPathList = new ArrayList<>();
		failedToCopyFilePathList = new ArrayList<>();
		failedToDeleteDirectoryPathList = new ArrayList<>();
		failedToDeleteFilePathList = new ArrayList<>();
	}

	@Override
	public void createDirectory(
			final String pathString) {

		final boolean success = FactoryFolderCreator.getInstance()
				.createDirectoryNoChecks(pathString, false, false);
		if (!success) {
			failedToCreateDirectoryPathList.add(pathString);
		}
	}

	@Override
	public void copyFile(
			final Path zipPath,
			final String fileSystemPathString,
			final boolean dstFileExists) {

		boolean success = false;
		try {
			Logger.printProgress("copying file:");
			Logger.printLine(zipPath);
			Logger.printLine("to:");
			Logger.printLine(fileSystemPathString);

			final boolean keepGoing;
			if (dstFileExists) {
				keepGoing = FactoryReadOnlyFlagClearer.getInstance()
						.clearReadOnlyFlagFileNoChecks(fileSystemPathString, false, true);
			} else {
				keepGoing = FactoryFolderCreator.getInstance()
						.createParentDirectories(fileSystemPathString, false, true);
			}
			if (keepGoing) {

				final List<CopyOption> copyOptionList = new ArrayList<>();
				copyOptionList.add(StandardCopyOption.REPLACE_EXISTING);
				copyOptionList.add(StandardCopyOption.COPY_ATTRIBUTES);
				final CopyOption[] copyOptionArray = copyOptionList.toArray(new CopyOption[] {});

				final Path dstFilePath = Paths.get(fileSystemPathString);
				Files.copy(zipPath, dstFilePath, copyOptionArray);
				success = true;
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
		}

		if (!success) {
			failedToCopyFilePathList.add(fileSystemPathString);
		}
	}

	@Override
	public void deleteFolder(
			final String pathString) {

		final boolean success = FactoryFolderDeleter.getInstance()
				.deleteFolderNoChecks(pathString, false, false);
		if (!success) {
			failedToDeleteDirectoryPathList.add(pathString);
		}
	}

	@Override
	public void deleteFile(
			final String pathString) {

		final boolean success = FactoryFileDeleter.getInstance()
				.deleteFileNoChecks(pathString, false, false);
		if (!success) {
			failedToDeleteFilePathList.add(pathString);
		}
	}

	@Override
	public void printErrors() {

		if (!(failedToCreateDirectoryPathList.isEmpty() &&
				failedToCopyFilePathList.isEmpty() &&
				failedToDeleteDirectoryPathList.isEmpty() &&
				failedToDeleteFilePathList.isEmpty())) {

			final StringBuilder sbMessage = new StringBuilder("archive created but:");
			if (!failedToCreateDirectoryPathList.isEmpty()) {
				sbMessage.append(System.lineSeparator()).append("failed to create ")
						.append(failedToCreateDirectoryPathList.size()).append(" folders");
			}
			if (!failedToCopyFilePathList.isEmpty()) {
				sbMessage.append(System.lineSeparator()).append("failed to copy ")
						.append(failedToCopyFilePathList.size()).append(" files");
			}
			if (!failedToDeleteDirectoryPathList.isEmpty()) {
				sbMessage.append(System.lineSeparator()).append("failed to delete ")
						.append(failedToDeleteDirectoryPathList.size()).append(" folders");
			}
			if (!failedToDeleteFilePathList.isEmpty()) {
				sbMessage.append(System.lineSeparator()).append("failed to delete ")
						.append(failedToDeleteFilePathList.size()).append(" files");
			}
			sbMessage.append('!');

			final String message = sbMessage.toString();
			new CustomAlertError("error!", message).showAndWait();
		}
	}
}
