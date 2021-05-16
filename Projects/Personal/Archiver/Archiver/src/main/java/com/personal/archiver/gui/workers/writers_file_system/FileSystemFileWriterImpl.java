package com.personal.archiver.gui.workers.writers_file_system;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.utils.gui_utils.alerts.CustomAlertError;
import com.utils.io.file_copiers.FactoryFileCopier;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.folder_deleters.FactoryFolderDeleter;

public class FileSystemFileWriterImpl implements FileSystemFileWriter {

	private final List<Path> failedToCreateDirectoryPathList;
	private final List<Path> failedToCopyFilePathList;
	private final List<Path> failedToDeleteDirectoryPathList;
	private final List<Path> failedToDeleteFilePathList;

	public FileSystemFileWriterImpl() {

		failedToCreateDirectoryPathList = new ArrayList<>();
		failedToCopyFilePathList = new ArrayList<>();
		failedToDeleteDirectoryPathList = new ArrayList<>();
		failedToDeleteFilePathList = new ArrayList<>();
	}

	@Override
	public void createDirectory(
			final Path path) {

		final boolean success = FactoryFolderCreator.getInstance()
				.createDirectoryNoChecks(path, false);
		if (!success) {
			failedToCreateDirectoryPathList.add(path);
		}
	}

	@Override
	public void copyFile(
			final Path zipPath,
			final Path fileSystemPath,
			final boolean destFileExists) {

		final boolean success = FactoryFileCopier.getInstance()
				.copyFileNoChecks(zipPath, fileSystemPath, destFileExists, true, false);
		if (!success) {
			failedToCopyFilePathList.add(fileSystemPath);
		}
	}

	@Override
	public void deleteFolder(
			final Path path) {

		final boolean success = FactoryFolderDeleter.getInstance()
				.deleteFolderNoChecks(path, false);
		if (!success) {
			failedToDeleteDirectoryPathList.add(path);
		}
	}

	@Override
	public void deleteFile(
			final Path path) {

		final boolean success = FactoryFileDeleter.getInstance()
				.deleteFileNoChecks(path, false);
		if (!success) {
			failedToDeleteFilePathList.add(path);
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
