package com.personal.archiver.gui.workers.writers_zip;

import java.nio.channels.FileChannel;
import java.nio.file.FileSystem;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.utils.gui.alerts.CustomAlertException;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicators;

class ZipFileWriterNetwork extends ZipFileWriterLocal {

	private final String outputPathString;

	ZipFileWriterNetwork(
			final String workingDirPathString,
			final FileSystem zipFileSystem,
			final String zipFilePathString,
			final String outputPathString) {

		super(workingDirPathString, zipFileSystem, zipFilePathString);

		this.outputPathString = outputPathString;
	}

	@Override
	public void closeZipFileSystem() {

		super.closeZipFileSystem();

		final String zipFilePathString = getZipFilePathString();
		copyFileUsingChannel(zipFilePathString, outputPathString);
		FactoryFileDeleter.getInstance().deleteFile(zipFilePathString, false, false);
	}

	private static void copyFileUsingChannel(
			final String srcPathString,
			final String dstPathString) {

		try (FileChannel sourceChannel = FileChannel.open(Paths.get(srcPathString), StandardOpenOption.READ);
				FileChannel dstChannel = FileChannel.open(
						Paths.get(dstPathString), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

			final long size = sourceChannel.size();
			final long blockSize = 64 * 1024 * 1024 - 32 * 1024;
			final int blockCount = (int) (size / blockSize);
			int totalBlockCount = blockCount;
			final long lastBlockEndPosition = blockCount * blockSize;
			final boolean lastBlock = lastBlockEndPosition < size;
			totalBlockCount++;
			for (int i = 0; i < blockCount; i++) {

				dstChannel.transferFrom(sourceChannel, i * blockSize, blockSize);
				ProgressIndicators.getInstance().update(i + 1, totalBlockCount);
			}

			if (lastBlock) {

				dstChannel.transferFrom(sourceChannel, lastBlockEndPosition, size - lastBlockEndPosition);
				ProgressIndicators.getInstance().update(1);
			}

		} catch (final Exception exc) {
			new CustomAlertException("error!", "failed to copy file:" + System.lineSeparator() +
					srcPathString + System.lineSeparator() + "to:" + System.lineSeparator() + dstPathString,
					exc).showAndWait();
			Logger.printException(exc);
		}
	}

	@Override
	public void deleteFiles() {

		super.deleteFiles();

		FactoryFileDeleter.getInstance().deleteFile(outputPathString, false, false);
	}
}
