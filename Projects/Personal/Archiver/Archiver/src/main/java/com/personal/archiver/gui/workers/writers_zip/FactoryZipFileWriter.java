package com.personal.archiver.gui.workers.writers_zip;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.utils.gui_utils.alerts.CustomAlertException;
import com.utils.io.ZipUtils;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public final class FactoryZipFileWriter {

	private FactoryZipFileWriter() {
	}

	public static ZipFileWriter newInstance(
			final Path workingDirPath,
			final Path outputPath) {

		ZipFileWriter zipFileWriter = null;
		try {
			FactoryFileDeleter.getInstance().deleteFile(outputPath, false);
			FactoryFolderCreator.getInstance().createParentDirectories(outputPath, true);

			final String outputPathUriString = outputPath.toUri().toString();
			final boolean localOutputPath = outputPathUriString.startsWith("file:///");
			if (localOutputPath) {

				final FileSystem zipFileSystem = ZipUtils.createNewZipFileSystem(outputPath, true);
				zipFileWriter = new ZipFileWriterLocal(workingDirPath, zipFileSystem, outputPath);

			} else {
				final String temporaryArchiveName =
						"ArchiverTemporaryArchive_" + StrUtils.createDateTimeString();
				final Path zipFilePath = Paths.get(System.getenv("TEMP"), temporaryArchiveName);
				FactoryFileDeleter.getInstance().deleteFile(zipFilePath, false);
				FactoryFolderCreator.getInstance().createParentDirectories(zipFilePath, false);
				final FileSystem zipFileSystem = ZipUtils.createNewZipFileSystem(zipFilePath, true);
				zipFileWriter = new ZipFileWriterNetwork(workingDirPath, zipFileSystem, zipFilePath, outputPath);
			}

		} catch (final Exception exc) {
			new CustomAlertException("error!",
					"failed to create ZipFileWriter!", exc).showAndWait();
			Logger.printException(exc);
		}
		return zipFileWriter;
	}
}
