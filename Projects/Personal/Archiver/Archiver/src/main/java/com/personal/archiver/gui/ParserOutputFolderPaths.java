package com.personal.archiver.gui;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.commons.lang3.SystemUtils;

import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.ReaderUtils;
import com.utils.io.StreamUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class ParserOutputFolderPaths {

	private static final String CACHED_DATA_FOLDER_PATH_STRING =
			PathUtils.computePath(SystemUtils.USER_HOME, "Archiver", "cached_data.txt");

	private final Deque<String> outputFolderPathStrings;

	ParserOutputFolderPaths() {

		outputFolderPathStrings = new ArrayDeque<>(12);
	}

	void parse() {

		if (IoUtils.fileExists(CACHED_DATA_FOLDER_PATH_STRING)) {

			Logger.printProgress("parsing output folder paths from:");
			Logger.printLine(CACHED_DATA_FOLDER_PATH_STRING);

			try (BufferedReader bufferedReader =
					ReaderUtils.openBufferedReader(CACHED_DATA_FOLDER_PATH_STRING)) {

				String line;
				while ((line = bufferedReader.readLine()) != null) {
					outputFolderPathStrings.add(line);
				}

			} catch (final Exception exc) {
				Logger.printException(exc);
			}
		}
	}

	void save(
			final String outputFolderPathStringToAdd) {

		Logger.printProgress("saving output folder paths to:");
		Logger.printLine(CACHED_DATA_FOLDER_PATH_STRING);

		if (!outputFolderPathStrings.contains(outputFolderPathStringToAdd)) {

			outputFolderPathStrings.addFirst(outputFolderPathStringToAdd);
			if (outputFolderPathStrings.size() > 10) {
				outputFolderPathStrings.removeLast();
			}
		}

		FactoryFolderCreator.getInstance()
				.createParentDirectories(CACHED_DATA_FOLDER_PATH_STRING, false, true);
		FactoryReadOnlyFlagClearer.getInstance()
				.clearReadOnlyFlagFile(CACHED_DATA_FOLDER_PATH_STRING, false, true);
		try (PrintStream printStream = StreamUtils.openPrintStream(CACHED_DATA_FOLDER_PATH_STRING)) {

			for (final String outputFolderPathString : outputFolderPathStrings) {

				printStream.print(outputFolderPathString);
				printStream.println();
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	Deque<String> getOutputFolderPathStrings() {
		return outputFolderPathStrings;
	}
}
