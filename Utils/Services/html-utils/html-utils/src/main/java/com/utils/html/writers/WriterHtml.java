package com.utils.html.writers;

import java.nio.file.Path;

public interface WriterHtml {

	void writeToFile(
			Path outputPath);

	String writeToString();
}
