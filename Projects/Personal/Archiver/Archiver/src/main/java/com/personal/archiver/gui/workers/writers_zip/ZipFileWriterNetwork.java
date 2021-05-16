package com.personal.archiver.gui.workers.writers_zip;

import java.nio.channels.FileChannel;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.utils.gui_utils.alerts.CustomAlertException;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicator;

class ZipFileWriterNetwork extends ZipFileWriterLocal {

	private final Path outputPath;

	ZipFileWriterNetwork(
			final Path workingDirPath,
			final FileSystem zipFileSystem,
			final Path zipFilePath,
			final Path outputPath) {

		super(workingDirPath, zipFileSystem, zipFilePath);

		this.outputPath = outputPath;
	}

	@Override
	public void closeZipFileSystem() {

		super.closeZipFileSystem();

		final Path zipFilePath = getZipFilePath();
		copyFileUsingChannel(zipFilePath, outputPath);
		FactoryFileDeleter.getInstance().deleteFile(zipFilePath, false);
	}

	private static void copyFileUsingChannel(
			final Path srcPath,
			final Path destPath) {

		try (FileChannel sourceChannel = FileChannel.open(srcPath, StandardOpenOption.READ);
				FileChannel destChannel = FileChannel.open(
						destPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

			final long size = sourceChannel.size();
			final long blockSize = 64 * 1024 * 1024 - 32 * 1024;
			final int blockCount = (int) (size / blockSize);
			int totalBlockCount = blockCount;
			final long lastBlockEndPosition = blockCount * blockSize;
			final boolean lastBlock = lastBlockEndPosition < size;
			totalBlockCount++;
			for (int i = 0; i < blockCount; i++) {

				destChannel.transferFrom(sourceChannel, i * blockSize, blockSize);
				ProgressIndicator.getInstance().update(i + 1, totalBlockCount);
			}

			if (lastBlock) {
				destChannel.transferFrom(sourceChannel, lastBlockEndPosition, size - lastBlockEndPosition);
				ProgressIndicator.getInstance().update(1);
			}

		} catch (final Exception exc) {
			new CustomAlertException("error!", "failed to copy file:" + System.lineSeparator() +
					srcPath + System.lineSeparator() + "to:" + System.lineSeparator() + destPath,
					exc).showAndWait();
			Logger.printException(exc);
		}
	}

	@Override
	public void deleteFiles() {

		super.deleteFiles();

		FactoryFileDeleter.getInstance().deleteFile(outputPath, false);
	}
}
