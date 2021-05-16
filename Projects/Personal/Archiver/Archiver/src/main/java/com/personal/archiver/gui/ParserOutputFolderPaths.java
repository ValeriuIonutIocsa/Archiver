package com.personal.archiver.gui;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;

import com.utils.io.IoUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class ParserOutputFolderPaths {

	private static final Path CACHED_DATA_FOLDER_PATH =
			Paths.get(System.getProperty("user.home"), "Archiver", "cached_data.txt");

	private final Deque<String> outputFolderPathStrings;

	ParserOutputFolderPaths() {

		outputFolderPathStrings = new ArrayDeque<>(12);
	}

	void parse() {

		if (IoUtils.fileExists(CACHED_DATA_FOLDER_PATH)) {

			Logger.printProgress("parsing output folder paths from:");
			Logger.printLine(CACHED_DATA_FOLDER_PATH);

			try (BufferedReader bufferedReader = Files.newBufferedReader(CACHED_DATA_FOLDER_PATH)) {

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
		Logger.printLine(CACHED_DATA_FOLDER_PATH);

		if (!outputFolderPathStrings.contains(outputFolderPathStringToAdd)) {

			outputFolderPathStrings.addFirst(outputFolderPathStringToAdd);
			if (outputFolderPathStrings.size() > 10) {
				outputFolderPathStrings.removeLast();
			}
		}

		FactoryFolderCreator.getInstance().createParentDirectories(CACHED_DATA_FOLDER_PATH, false);
		FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(CACHED_DATA_FOLDER_PATH, false);
		try (PrintWriter printWriter = new PrintWriter(
				Files.newBufferedWriter(CACHED_DATA_FOLDER_PATH))) {

			for (final String outputFolderPathString : outputFolderPathStrings) {
				printWriter.println(outputFolderPathString);
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
