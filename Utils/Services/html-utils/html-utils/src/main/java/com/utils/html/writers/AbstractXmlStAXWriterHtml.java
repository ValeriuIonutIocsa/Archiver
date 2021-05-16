package com.utils.html.writers;

import java.io.OutputStream;

import com.utils.html.sections.HtmlSectionPlainText;
import com.utils.xml.stax.AbstractXmlStAXWriter;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractXmlStAXWriterHtml extends AbstractXmlStAXWriter {

	protected AbstractXmlStAXWriterHtml(
			final OutputStream outputStream) {
		super(outputStream, "");
	}

	@Override
	protected void write() {

		writePlainText("<!-- saved from url=(0014)about:internet -->");
		writeStartElement("html");

		writeStartElement("head");
		writeHead(this);
		writeEndElement("head");

		writeStartElement("body");
		writeBodyAttributes(this);
		writeBody(this);
		writeEndElement("body");

		writeEndElement("html");
	}

	private void writeHead(
			final XmlStAXWriter xmlStAXWriter) {

		xmlStAXWriter.writeStartElement("meta");
		xmlStAXWriter.writeAttribute("http-equiv", "X-UA-Compatible");
		xmlStAXWriter.writeAttribute("content", "IE=10; IE=9; IE=8; IE=7; IE=EDGE");
		xmlStAXWriter.writeEndElement("meta");

		xmlStAXWriter.writeStartElement("style");
		xmlStAXWriter.writeAttribute("type", "text/css");
		xmlStAXWriter.writeAttribute("xml:space", "preserve");

		final String cssString = createCssString();
		new HtmlSectionPlainText(cssString).write(xmlStAXWriter);

		xmlStAXWriter.writeEndElement("style");
	}

	protected abstract String createCssString();

	protected void writeBodyAttributes(
			final XmlStAXWriter xmlStAXWriter) {
	}

	protected abstract void writeBody(
			XmlStAXWriter xmlStAXWriter);
}
