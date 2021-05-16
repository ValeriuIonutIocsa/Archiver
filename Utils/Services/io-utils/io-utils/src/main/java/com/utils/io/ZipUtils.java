package com.utils.io;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.concurrency.no_progress.ConcurrencyUtilsRegular;
import com.utils.io.file_copiers.FactoryFileCopier;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.folder_deleters.FactoryFolderDeleter;
import com.utils.log.Logger;

public final class ZipUtils {

	private ZipUtils() {
	}

	@ApiMethod
	public static boolean createZip(
			final Path srcFolderPath,
			final Path zipFilePath,
			final boolean useTempFile,
			final boolean deleteExisting,
			final int threadCount,
			final boolean updateFileTimes,
			final boolean verbose) {

		boolean success = false;

		Logger.printProgress("creating ZIP archive:");
		Logger.printLine(zipFilePath);

		if (!IoUtils.directoryExists(srcFolderPath)) {
			Logger.printWarning("the source folder does not exist:" +
					System.lineSeparator() + srcFolderPath);

		} else {
			if (deleteExisting && IoUtils.fileExists(zipFilePath)) {
				FactoryFileDeleter.getInstance().deleteFile(zipFilePath, true);
			}

			FactoryFolderCreator.getInstance().createParentDirectories(zipFilePath, true);

			try (FileSystem zipFileSystem = createNewZipFileSystem(zipFilePath, useTempFile)) {

				final List<Runnable> runnableList = new ArrayList<>();
				Files.walkFileTree(srcFolderPath, new SimpleFileVisitor<>() {

					@Override
					public FileVisitResult visitFile(
							final Path filePath,
							final BasicFileAttributes attrs) throws IOException {

						runnableList.add(() -> {

							try {
								if (verbose) {
									Logger.printLine("zipping file: " + filePath);
								}

								final Path relativeFilePath = srcFolderPath.relativize(filePath);
								final Path zipFilePath = zipFileSystem.getPath(relativeFilePath.toString());
								final Path zipFileParentFolderPath = zipFilePath.getParent();
								if (zipFileParentFolderPath != null &&
										!Files.isDirectory(zipFileParentFolderPath)) {
									synchronized (this) {
										Files.createDirectories(zipFileParentFolderPath);
									}
								}
								Files.copy(filePath, zipFilePath, StandardCopyOption.REPLACE_EXISTING,
										StandardCopyOption.COPY_ATTRIBUTES);
								if (updateFileTimes) {
									Files.setLastModifiedTime(zipFilePath, FileTime.from(Instant.now()));
								}

							} catch (final Exception exc) {
								Logger.printError("failed to zip file:" + System.lineSeparator() + filePath);
								Logger.printException(exc);
							}
						});

						return super.visitFile(filePath, attrs);
					}
				});
				if (!runnableList.isEmpty()) {
					new ConcurrencyUtilsRegular(threadCount).executeMultiThreadedTask(runnableList);
				}

				Logger.printStatus("Finished creating ZIP archive.");
				success = true;

			} catch (final Exception exc) {
				Logger.printError("failed to create ZIP archive:" +
						System.lineSeparator() + zipFilePath);
				Logger.printException(exc);
			}
		}
		return success;
	}

	@ApiMethod
	public static boolean extractZip(
			final Path zipFilePath,
			final Path dstFolderPath,
			final boolean useTempFile,
			final boolean deleteExisting,
			final int threadCount,
			final boolean updateFileTimes,
			final boolean verbose) {

		boolean success = false;

		Logger.printProgress("extracting ZIP archive:");
		Logger.printLine(zipFilePath);

		if (!IoUtils.fileExists(zipFilePath)) {
			Logger.printWarning("the ZIP archive does not exist:" +
					System.lineSeparator() + zipFilePath);

		} else {
			if (deleteExisting) {
				FactoryFolderDeleter.getInstance().cleanFolder(dstFolderPath, true);
			}

			FactoryFolderCreator.getInstance().createDirectories(dstFolderPath, true);

			try (FileSystem zipFileSystem = openZipFileSystem(zipFilePath, useTempFile)) {

				final List<Runnable> runnableList = new ArrayList<>();
				final Path root = zipFileSystem.getPath("/");
				Files.walkFileTree(root, new SimpleFileVisitor<>() {

					@Override
					public FileVisitResult visitFile(
							final Path filePath,
							final BasicFileAttributes attrs) throws IOException {

						runnableList.add(() -> {

							try {
								if (verbose) {
									Logger.printLine("extracting file: " + filePath);
								}

								final Path dstFilePath = Paths.get(dstFolderPath.toString(), filePath.toString());
								final Path dstFileParentFolderPath = dstFilePath.getParent();
								if (Files.notExists(dstFileParentFolderPath)) {
									synchronized (this) {
										Files.createDirectories(dstFileParentFolderPath);
									}
								}
								FactoryFileCopier.getInstance().copyFile(filePath, dstFilePath, true, true);
								if (updateFileTimes) {
									Files.setLastModifiedTime(dstFilePath, FileTime.from(Instant.now()));
								}

							} catch (final Exception exc) {
								Logger.printError("failed to extract file:" + System.lineSeparator() + filePath);
								Logger.printException(exc);
							}
						});

						return super.visitFile(filePath, attrs);
					}
				});

				if (!runnableList.isEmpty()) {
					new ConcurrencyUtilsRegular(threadCount).executeMultiThreadedTask(runnableList);
				}
				Logger.printStatus("Finished extracting ZIP archive.");
				success = true;

			} catch (final Exception exc) {
				Logger.printError("failed to extract ZIP archive:" +
						System.lineSeparator() + zipFilePath);
				Logger.printException(exc);
			}
		}
		return success;
	}

	@ApiMethod
	public static FileSystem createNewZipFileSystem(
			final Path zipFilePath,
			final boolean useTempFile) throws Exception {

		final Map<String, Object> env = new HashMap<>();
		env.put("create", "true");
		env.put("useTempFile", useTempFile);
		return createZipFileSystem(zipFilePath, env);
	}

	@ApiMethod
	public static FileSystem openZipFileSystem(
			final Path zipFilePath,
			final boolean useTempFile) throws Exception {

		final Map<String, Object> env = new HashMap<>();
		env.put("create", "false");
		env.put("useTempFile", useTempFile);
		return createZipFileSystem(zipFilePath, env);
	}

	private static FileSystem createZipFileSystem(
			final Path zipFilePath,
			final Map<String, ?> env) throws Exception {

		final String archiveUriString;
		final String zipFilePathUriString = zipFilePath.toUri().toString();
		if (zipFilePathUriString.startsWith("file:///")) {
			archiveUriString = "jar:" + zipFilePathUriString;
		} else {
			archiveUriString = "jar:" + StringUtils.replaceOnce(zipFilePathUriString, "file://", "file://///");
		}
		final URI archiveUri = URI.create(archiveUriString);
		return FileSystems.newFileSystem(archiveUri, env);
	}
}
