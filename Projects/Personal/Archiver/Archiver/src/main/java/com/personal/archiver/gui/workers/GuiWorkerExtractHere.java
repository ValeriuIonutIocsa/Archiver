package com.personal.archiver.gui.workers;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.personal.archiver.gui.data.FileToArchive;
import com.personal.archiver.gui.workers.runnables.TreeRunnable;
import com.personal.archiver.gui.workers.runnables.TreeRunnableExecutor;
import com.personal.archiver.gui.workers.writers_file_system.FileSystemFileWriter;
import com.personal.archiver.gui.workers.writers_file_system.FileSystemFileWriterImpl;
import com.utils.gui.alerts.CustomAlertException;
import com.utils.gui.workers.AbstractGuiWorker;
import com.utils.gui.workers.ControlDisabler;
import com.utils.io.IoUtils;
import com.utils.io.ListFileUtils;
import com.utils.io.PathUtils;
import com.utils.io.zip.ZipUtils;
import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicators;

import javafx.scene.Scene;

public class GuiWorkerExtractHere
		extends AbstractGuiWorker implements CloseableGuiWorker {

	private final FileToArchive fileToArchive;
	private String fileSystemRootPathString;
	private final AtomicInteger runnableCount;
	private final FileSystemFileWriter fileSystemFileWriter;

	private final boolean closeOnCompletion;

	public GuiWorkerExtractHere(
			final Scene scene,
			final ControlDisabler controlDisabler,
			final FileToArchive fileToArchive,
			final boolean closeOnCompletion) {

		super(scene, controlDisabler);

		this.fileToArchive = fileToArchive;

		runnableCount = new AtomicInteger();
		fileSystemFileWriter = new FileSystemFileWriterImpl();

		this.closeOnCompletion = closeOnCompletion;
	}

	@Override
	protected void work() {

		ProgressIndicators.getInstance().update(0);
		Logger.printProgress("extracting archive...");

		final String archiveFilePathString = fileToArchive.getFilePathString();
		if (IoUtils.fileExists(archiveFilePathString)) {

			try (FileSystem fileSystem =
					ZipUtils.openZipFileSystem(archiveFilePathString, true)) {

				final TreeRunnable treeRunnableRoot = new TreeRunnable(null);

				final Path zipFileRootPath = fileSystem.getPath("/");
				fileSystemRootPathString = PathUtils.computeParentPath(archiveFilePathString);
				fillRunnableListRec(true, zipFileRootPath, fileSystemRootPathString, treeRunnableRoot);

				new TreeRunnableExecutor(treeRunnableRoot, runnableCount).work();

			} catch (final Exception exc) {
				new CustomAlertException("error!",
						"failed to generate archive!", exc).showAndWait();
				Logger.printException(exc);

			} finally {
				fileSystemFileWriter.printErrors();
			}
		}
	}

	private void fillRunnableListRec(
			final boolean firstLevel,
			final Path zipParentFolderPath,
			final String fileSystemParentFolderPathString,
			final TreeRunnable treeRunnable) {

		final Map<String, String> fileSystemFolderMap = new HashMap<>();
		final Map<String, String> fileSystemFileMap = new HashMap<>();
		if (fileSystemParentFolderPathString != null) {

			ListFileUtils.visitFiles(fileSystemParentFolderPathString,
					dirPath -> {

						final String fileSystemPathString = dirPath.toString();
						final String fileSystemRelativePathString =
								PathUtils.computeRelativePath(fileSystemRootPathString, fileSystemPathString);
						fileSystemFolderMap.put(fileSystemRelativePathString, fileSystemPathString);
					},
					filePath -> {

						final String fileSystemPathString = filePath.toString();
						final String fileSystemRelativePathString =
								PathUtils.computeRelativePath(fileSystemRootPathString, fileSystemPathString);
						fileSystemFileMap.put(fileSystemRelativePathString, fileSystemPathString);
					});
		}

		final List<Path> zipPathList = new ArrayList<>();
		try {
			try (Stream<Path> fileListStream = Files.list(zipParentFolderPath)) {
				fileListStream.forEach(zipPathList::add);
			}

		} catch (final Exception exc) {
			Logger.printError("failed to list files inside folder:" +
					System.lineSeparator() + zipParentFolderPath);
			Logger.printException(exc);
		}

		for (final Path zipPath : zipPathList) {

			if (Files.isDirectory(zipPath)) {

				final String relativePathString = computeZipFolderRelativePathString(zipPath);
				final String fileSystemFolderPathString = fileSystemFolderMap.get(relativePathString);
				final TreeRunnable treeRunnableRec;
				if (fileSystemFolderPathString == null) {

					final String newFileSystemFolderPathString =
							PathUtils.computePath(fileSystemRootPathString, relativePathString);
					treeRunnableRec = addTreeRunnable(treeRunnable,
							() -> fileSystemFileWriter.createDirectory(newFileSystemFolderPathString));

				} else {
					treeRunnableRec = treeRunnable;
				}
				fillRunnableListRec(false, zipPath, fileSystemFolderPathString, treeRunnableRec);
				fileSystemFolderMap.remove(relativePathString);

			} else {
				final String relativePathString = computeZipFileRelativePathString(zipPath);
				String fileSystemPathString = fileSystemFileMap.get(relativePathString);
				final boolean dstFileExists;
				if (fileSystemPathString == null) {

					fileSystemPathString =
							PathUtils.computePath(fileSystemRootPathString, relativePathString);
					dstFileExists = false;

				} else {
					dstFileExists = true;
				}
				final String finalFileSystemPathString = fileSystemPathString;
				addTreeRunnable(treeRunnable, () -> fileSystemFileWriter.copyFile(
						zipPath, finalFileSystemPathString, dstFileExists));
				fileSystemFileMap.remove(relativePathString);
			}
		}

		if (!firstLevel) {

			for (final String fileSystemFolderPathString : fileSystemFolderMap.values()) {
				addTreeRunnable(treeRunnable,
						() -> fileSystemFileWriter.deleteFolder(fileSystemFolderPathString));
			}
			for (final String fileSystemFilePathString : fileSystemFileMap.values()) {
				addTreeRunnable(treeRunnable,
						() -> fileSystemFileWriter.deleteFile(fileSystemFilePathString));
			}
		}
	}

	private static String computeZipFolderRelativePathString(
			final Path zipPath) {

		final String zipPathString = zipPath.toString();
		final String relativePathString = zipPathString.substring(1, zipPathString.length() - 1);
		return StringUtils.replaceChars(relativePathString, '/', '\\');
	}

	private static String computeZipFileRelativePathString(
			final Path zipPath) {

		final String zipPathString = zipPath.toString();
		final String relativePathString = zipPathString.substring(1);
		return StringUtils.replaceChars(relativePathString, '/', '\\');
	}

	private TreeRunnable addTreeRunnable(
			final TreeRunnable treeRunnableParent,
			final Runnable runnable) {

		final TreeRunnable treeRunnable = new TreeRunnable(runnable);
		runnableCount.incrementAndGet();
		treeRunnableParent.getChildTreeRunnableList().add(treeRunnable);
		return treeRunnable;
	}

	@Override
	protected void error() {
	}

	@Override
	protected void finish() {

		ProgressIndicators.getInstance().update(0);

		if (closeOnCompletion) {
			getScene().getWindow().hide();
		}
	}

	@Override
	public void close() {
	}
}
