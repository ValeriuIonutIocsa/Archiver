package com.personal.archiver.gui.workers;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.personal.archiver.gui.data.FileToArchive;
import com.personal.archiver.gui.workers.runnables.TreeRunnable;
import com.personal.archiver.gui.workers.runnables.TreeRunnableExecutor;
import com.personal.archiver.gui.workers.writers_file_system.FileSystemFileWriter;
import com.personal.archiver.gui.workers.writers_file_system.FileSystemFileWriterImpl;
import com.utils.gui_utils.alerts.CustomAlertException;
import com.utils.gui_utils.workers.ComponentDisabler;
import com.utils.gui_utils.workers.GuiWorker;
import com.utils.io.IoUtils;
import com.utils.io.ZipUtils;
import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicator;

import javafx.scene.Scene;
import javafx.scene.control.CheckBox;

public class GuiWorkerExtractHere extends GuiWorker implements CloseableGuiWorker {

	private final FileToArchive fileToArchive;
	private Path fileSystemRootPath;
	private String fileSystemRootPathString;
	private final AtomicInteger runnableCount;
	private final FileSystemFileWriter fileSystemFileWriter;

	private final CheckBox checkBoxCloseOnCompletion;

	public GuiWorkerExtractHere(
			final Scene scene,
			final ComponentDisabler componentDisabler,
			final FileToArchive fileToArchive,
			final CheckBox checkBoxCloseOnCompletion) {

		super(scene, componentDisabler);

		this.fileToArchive = fileToArchive;

		runnableCount = new AtomicInteger();
		fileSystemFileWriter = new FileSystemFileWriterImpl();

		this.checkBoxCloseOnCompletion = checkBoxCloseOnCompletion;
	}

	@Override
	protected void work() {

		ProgressIndicator.getInstance().update(0);
		Logger.printProgress("extracting archive...");

		final Path archiveFilePath = fileToArchive.getFilePath();
		if (IoUtils.fileExists(archiveFilePath)) {

			try (FileSystem fileSystem = ZipUtils.openZipFileSystem(archiveFilePath, true)) {

				final TreeRunnable treeRunnableRoot = new TreeRunnable(null);

				final Path zipRootPath = fileSystem.getPath("/");
				fileSystemRootPath = archiveFilePath.getParent();
				fileSystemRootPathString = fileSystemRootPath.toString();
				fillRunnableListRec(true, zipRootPath, fileSystemRootPath, treeRunnableRoot);

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
			final Path fileSystemParentFolderPath,
			final TreeRunnable treeRunnable) {

		final Map<String, Path> fileSystemFolderMap = new HashMap<>();
		final Map<String, Path> fileSystemFileMap = new HashMap<>();
		if (fileSystemParentFolderPath != null) {

			final List<Path> fileSystemPathList = IoUtils.listFiles(fileSystemParentFolderPath);
			for (final Path fileSystemPath : fileSystemPathList) {

				final Path fileSystemRelativePath = fileSystemRootPath.relativize(fileSystemPath);
				final String relativePathString = fileSystemRelativePath.toString();
				if (Files.isDirectory(fileSystemPath)) {
					fileSystemFolderMap.put(relativePathString, fileSystemPath);
				} else {
					fileSystemFileMap.put(relativePathString, fileSystemPath);
				}
			}
		}

		final List<Path> zipPathList = IoUtils.listFiles(zipParentFolderPath);
		for (final Path zipPath : zipPathList) {

			if (Files.isDirectory(zipPath)) {

				final String relativePathString = computeZipFolderRelativePathString(zipPath);
				final Path fileSystemFolderPath = fileSystemFolderMap.getOrDefault(relativePathString, null);
				final TreeRunnable treeRunnableRec;
				if (fileSystemFolderPath == null) {
					final Path newFileSystemFolderPath =
							Paths.get(fileSystemRootPathString, relativePathString);
					treeRunnableRec = addTreeRunnable(treeRunnable,
							() -> fileSystemFileWriter.createDirectory(newFileSystemFolderPath));
				} else {
					treeRunnableRec = treeRunnable;
				}
				fillRunnableListRec(false, zipPath, fileSystemFolderPath, treeRunnableRec);
				fileSystemFolderMap.remove(relativePathString);

			} else {
				final String relativePathString = computeZipFileRelativePathString(zipPath);
				Path fileSystemPath = fileSystemFileMap.getOrDefault(relativePathString, null);
				final boolean destFileExists;
				if (fileSystemPath == null) {
					fileSystemPath = Paths.get(fileSystemRootPathString, relativePathString);
					destFileExists = false;
				} else {
					destFileExists = true;
				}
				final Path finalFileSystemPath = fileSystemPath;
				addTreeRunnable(treeRunnable, () -> fileSystemFileWriter.copyFile(
						zipPath, finalFileSystemPath, destFileExists));
				fileSystemFileMap.remove(relativePathString);
			}
		}

		if (!firstLevel) {
			for (final Path fileSystemFolderPath : fileSystemFolderMap.values()) {
				addTreeRunnable(treeRunnable, () -> fileSystemFileWriter.deleteFolder(fileSystemFolderPath));
			}
			for (final Path fileSystemFilePath : fileSystemFileMap.values()) {
				addTreeRunnable(treeRunnable, () -> fileSystemFileWriter.deleteFile(fileSystemFilePath));
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

		ProgressIndicator.getInstance().update(0);

		if (checkBoxCloseOnCompletion != null) {
			final boolean closeOnCompletion = checkBoxCloseOnCompletion.isSelected();
			if (closeOnCompletion) {
				getScene().getWindow().hide();
			}
		}
	}

	@Override
	public void close() {
	}
}
