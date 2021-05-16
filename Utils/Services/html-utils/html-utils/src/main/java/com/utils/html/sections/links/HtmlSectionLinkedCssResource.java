package com.utils.html.sections.links;

import com.utils.html.sections.HtmlSectionAbstr;
import com.utils.io.IoUtils;
import com.utils.xml.stax.XmlStAXWriter;

public class HtmlSectionLinkedCssResource extends HtmlSectionAbstr {

	private final String cssResourceFilePath;

	public HtmlSectionLinkedCssResource(
			final String cssResourceFilePath) {

		this.cssResourceFilePath = cssResourceFilePath;
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {

		xmlStAXWriter.writeStartElement("link");
		xmlStAXWriter.writeAttribute("type", "text/css");
		xmlStAXWriter.writeAttribute("rel", "stylesheet");
		xmlStAXWriter.writeAttribute("href", IoUtils.resourceToUrl(cssResourceFilePath).toExternalForm());

		xmlStAXWriter.writeEndElement("link");
	}
}
