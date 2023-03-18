package com.personal.archiver.gui.workers.writers_zip;

import java.nio.file.FileSystem;
import java.nio.file.Paths;

import com.utils.gui.alerts.CustomAlertException;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.zip.ZipUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public final class FactoryZipFileWriter {

	private FactoryZipFileWriter() {
	}

	public static ZipFileWriter newInstance(
			final String workingDirPathString,
			final String outputPathString) {

		ZipFileWriter zipFileWriter = null;
		try {
			FactoryFileDeleter.getInstance().deleteFile(outputPathString, false);
			FactoryFolderCreator.getInstance().createParentDirectories(outputPathString, true);

			final String outputPathUriString = Paths.get(outputPathString).toUri().toString();
			final boolean localOutputPath = outputPathUriString.startsWith("file:///");
			if (localOutputPath) {

				final FileSystem zipFileSystem = ZipUtils.createNewZipFileSystem(outputPathString, true);
				zipFileWriter = new ZipFileWriterLocal(workingDirPathString, zipFileSystem, outputPathString);

			} else {
				final String temporaryArchiveName =
						"ArchiverTemporaryArchive_" + StrUtils.createDateTimeString();
				final String zipFilePathString =
						Paths.get(System.getenv("TEMP"), temporaryArchiveName).toString();
				FactoryFileDeleter.getInstance().deleteFile(zipFilePathString, false);
				FactoryFolderCreator.getInstance().createParentDirectories(zipFilePathString, false);
				final FileSystem zipFileSystem = ZipUtils.createNewZipFileSystem(zipFilePathString, true);
				zipFileWriter = new ZipFileWriterNetwork(
						workingDirPathString, zipFileSystem, zipFilePathString, outputPathString);
			}

		} catch (final Exception exc) {
			new CustomAlertException("error!",
					"failed to create ZipFileWriter", exc).showAndWait();
			Logger.printException(exc);
		}
		return zipFileWriter;
	}
}
