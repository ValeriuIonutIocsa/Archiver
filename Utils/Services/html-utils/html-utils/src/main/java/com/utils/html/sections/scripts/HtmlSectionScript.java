package com.utils.html.sections.scripts;

import com.utils.html.sections.HtmlSectionAbstr;
import com.utils.html.sections.HtmlSectionPlainText;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class HtmlSectionScript extends HtmlSectionAbstr {

	HtmlSectionScript() {
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {

		xmlStAXWriter.writeStartElement("script");
		xmlStAXWriter.writeAttribute("type", "text/javascript");

		final String jsScriptContent = createJsScriptContent();
		new HtmlSectionPlainText(jsScriptContent).write(xmlStAXWriter);

		xmlStAXWriter.writeEndElement("script");
	}

	protected abstract String createJsScriptContent();
}
