package com.utils.gui_utils.clipboard;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.utils.annotations.ApiMethod;
import com.utils.io.IoUtils;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public final class ClipboardUtils {

	private ClipboardUtils() {
	}

	@ApiMethod
	public static void putStringInClipBoard(
			final String string) {

		final boolean commandLineSuccess = runFromCommandLine(string);
		if (!commandLineSuccess) {

			new Thread(() -> Platform.runLater(() -> {
				final ClipboardContent clipboardContent = new ClipboardContent();
				clipboardContent.putString(string);
				Clipboard.getSystemClipboard().setContent(clipboardContent);
			})).start();
		}
	}

	private static boolean runFromCommandLine(
			final String string) {

		boolean success = false;
		final Path tmpFilePath = Paths.get(System.getProperty("user.home"),
				"clip_" + System.currentTimeMillis() + ".txt");
		try {
			FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(tmpFilePath, true);
			IoUtils.writeStringToFile(tmpFilePath, string, StandardCharsets.UTF_16LE);

			final List<String> commandList = new ArrayList<>();
			commandList.add("cmd");
			commandList.add("/c");
			commandList.add("type");
			commandList.add(tmpFilePath.toString());
			commandList.add("|");
			commandList.add("clip");

			final ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command(commandList);
			final File nullFile = new File("NUL");
			processBuilder.redirectOutput(nullFile);
			processBuilder.redirectError(nullFile);
			final Process process = processBuilder.start();
			process.waitFor();
			final int exitValue = process.exitValue();
			success = exitValue == 0;

		} catch (final Exception ignored) {
		} finally {
			FactoryFileDeleter.getInstance().deleteFile(tmpFilePath, true);
		}
		return success;
	}
}
