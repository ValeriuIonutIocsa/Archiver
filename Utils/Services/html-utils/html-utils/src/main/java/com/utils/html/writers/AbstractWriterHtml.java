package com.utils.html.writers;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.utils.html.sections.HtmlSection;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractWriterHtml implements WriterHtml {

	protected AbstractWriterHtml() {
	}

	@Override
	public void writeToFile(
			final Path outputPath) {

		FactoryFolderCreator.getInstance().createParentDirectories(outputPath, true);
		FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(outputPath, true);

		try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(outputPath))) {
			write(outputStream);

		} catch (final Exception exc) {
			Logger.printError("failed to write HTML to file:" + System.lineSeparator() + outputPath);
			Logger.printException(exc);
		}
	}

	@Override
	public String writeToString() {

		String str;
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			write(byteArrayOutputStream);
			str = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());

		} catch (final Exception exc) {
			Logger.printError("failed to write HTML to string!");
			Logger.printException(exc);
			str = "";
		}
		return str;
	}

	private void write(
			final OutputStream outputStream) {

		new AbstractXmlStAXWriterHtml(outputStream) {

			@Override
			protected String createCssString() {
				return AbstractWriterHtml.this.createCssString();
			}

			@Override
			protected void writeBody(
					final XmlStAXWriter xmlStAXWriter) {
				AbstractWriterHtml.this.writeBody(xmlStAXWriter);
			}
		}.writeXml();
	}

	protected abstract String createCssString();

	private void writeBody(
			final XmlStAXWriter xmlStAXWriter) {

		final List<HtmlSection> htmlSectionList = new ArrayList<>();
		fillBodyHtmlSectionList(htmlSectionList);

		for (final HtmlSection htmlSectionBody : htmlSectionList) {
			htmlSectionBody.write(xmlStAXWriter);
		}

		xmlStAXWriter.writePlainText(System.lineSeparator());
	}

	protected abstract void fillBodyHtmlSectionList(
			List<HtmlSection> htmlSectionList);

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
