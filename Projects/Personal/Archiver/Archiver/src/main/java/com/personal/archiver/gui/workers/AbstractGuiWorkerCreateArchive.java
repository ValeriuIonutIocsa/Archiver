package com.personal.archiver.gui.workers;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.personal.archiver.gui.data.FileToArchive;
import com.personal.archiver.gui.workers.writers_zip.FactoryZipFileWriter;
import com.personal.archiver.gui.workers.writers_zip.ZipFileWriter;
import com.utils.gui_utils.alerts.CustomAlertError;
import com.utils.gui_utils.alerts.CustomAlertException;
import com.utils.gui_utils.workers.ComponentDisabler;
import com.utils.gui_utils.workers.GuiWorker;
import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicator;

import javafx.scene.Scene;

public abstract class AbstractGuiWorkerCreateArchive extends GuiWorker implements CloseableGuiWorker {

	private final Path workingDirPath;
	private final Path outputPath;

	private ZipFileWriter zipFileWriter;
	private int totalItemsToCopyCount;

	AbstractGuiWorkerCreateArchive(
			final Scene scene,
			final ComponentDisabler componentDisabler,
			final Path workingDirPath,
			final Path outputPath) {

		super(scene, componentDisabler);

		this.workingDirPath = workingDirPath;
		this.outputPath = outputPath;
	}

	@Override
	protected void work() {

		ProgressIndicator.getInstance().update(0);
		Logger.printProgress("creating archive...");

		try {
			zipFileWriter = FactoryZipFileWriter.newInstance(workingDirPath, outputPath);
			if (zipFileWriter != null) {

				final AtomicInteger itemsToCopyCount = new AtomicInteger();
				final FileToArchive fileToArchiveRoot = createFileToArchiveRoot();
				final List<FileToArchive> childrenList = fileToArchiveRoot.getChildrenList();
				if (childrenList != null) {
					for (final FileToArchive fileToArchive : childrenList) {
						computeSelectedItemCountRec(fileToArchive, itemsToCopyCount);
					}
				}
				totalItemsToCopyCount = itemsToCopyCount.get();

				final LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>();

				if (childrenList != null) {
					for (final FileToArchive fileToArchive : childrenList) {
						linkedBlockingQueue.add(() -> copyFileToZipRunnable(
								fileToArchive, linkedBlockingQueue, itemsToCopyCount));
					}
				}

				final CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);
				for (int i = 0; i < THREAD_COUNT; i++) {

					new Thread(() -> {

						while (true) {

							final int itemsToCopyCountValue = itemsToCopyCount.get();
							if (itemsToCopyCountValue == 0) {
								countDownLatch.countDown();
								break;

							} else {
								try {
									final Runnable runnable = linkedBlockingQueue.poll(100, TimeUnit.MILLISECONDS);
									if (runnable != null) {
										runnable.run();
									}

								} catch (final Exception exc) {
									Logger.printException(exc);
								}
							}
						}
					}).start();
				}
				countDownLatch.await();
			}

		} catch (final Exception exc) {
			new CustomAlertException("error!",
					"failed to generate archive!", exc).showAndWait();
			Logger.printException(exc);

		} finally {
			if (zipFileWriter != null) {
				zipFileWriter.closeZipFileSystem();
				zipFileWriter.printErrors();
			}
		}
	}

	abstract FileToArchive createFileToArchiveRoot();

	private static int computeSelectedItemCountRec(
			final FileToArchive fileToArchive,
			final AtomicInteger itemsToCopyCount) {

		int selectedItemCount = 0;
		final boolean selected = fileToArchive.isSelected();
		if (selected) {
			selectedItemCount++;
		}

		final boolean folder = fileToArchive.isFolder();
		if (folder) {

			List<FileToArchive> childrenList = fileToArchive.getChildrenList();
			if (selected && childrenList == null) {

				fileToArchive.fillChildrenList();
				childrenList = fileToArchive.getChildrenList();
			}
			if (childrenList != null) {
				for (final FileToArchive fileToArchiveChild : childrenList) {
					selectedItemCount += computeSelectedItemCountRec(fileToArchiveChild, itemsToCopyCount);
				}
			}
		}

		fileToArchive.setSelectedItemCount(selectedItemCount);
		if (selectedItemCount > 0) {
			itemsToCopyCount.incrementAndGet();
		}
		return selectedItemCount;
	}

	private void copyFileToZipRunnable(
			final FileToArchive fileToArchive,
			final LinkedBlockingQueue<Runnable> linkedBlockingQueue,
			final AtomicInteger itemsToCopyCount) {

		final int selectedItemCount = fileToArchive.getSelectedItemCount();
		if (selectedItemCount > 0) {

			final boolean folder = fileToArchive.isFolder();
			if (folder) {

				zipFileWriter.copyFolderToZip(fileToArchive);
				decrementItemCount(itemsToCopyCount);

				final List<FileToArchive> childrenList = fileToArchive.getChildrenList();
				if (childrenList != null) {
					for (final FileToArchive fileToArchiveChild : childrenList) {
						linkedBlockingQueue.add(() -> copyFileToZipRunnable(
								fileToArchiveChild, linkedBlockingQueue, itemsToCopyCount));
					}
				}

			} else {
				zipFileWriter.copyFileToZip(fileToArchive);
				decrementItemCount(itemsToCopyCount);
			}
		}
	}

	private void decrementItemCount(
			final AtomicInteger itemsToCopyCount) {

		final int itemsToCopyCountValue = itemsToCopyCount.getAndDecrement();
		final int copiedItemsCount = totalItemsToCopyCount - itemsToCopyCountValue;
		ProgressIndicator.getInstance().update(copiedItemsCount, totalItemsToCopyCount);
	}

	@Override
	protected void error() {
		new CustomAlertError("error!", "error occurred while creating archive!").showAndWait();
	}

	@Override
	protected void finish() {
		ProgressIndicator.getInstance().update(0);
	}

	@Override
	public void close() {

		if (zipFileWriter != null) {
			zipFileWriter.deleteFiles();
		}
	}
}
