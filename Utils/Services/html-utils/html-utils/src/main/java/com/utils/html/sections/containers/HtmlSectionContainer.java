package com.utils.html.sections.containers;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.utils.html.sections.HtmlSection;
import com.utils.html.sections.HtmlSectionAbstr;
import com.utils.xml.stax.XmlStAXWriter;
import com.utils.xml.stax.AbstractXmlStAXWriter;

public abstract class HtmlSectionContainer extends HtmlSectionAbstr {

	protected HtmlSectionContainer() {
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {

		final List<HtmlSection> htmlSectionList = new ArrayList<>();
		fillHtmlSectionList(htmlSectionList);

		for (final HtmlSection htmlSection : htmlSectionList) {
			htmlSection.write(xmlStAXWriter);
		}
	}

	protected abstract void fillHtmlSectionList(
			List<HtmlSection> htmlSectionList);

	public String writeToString() {

		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		new AbstractXmlStAXWriter(byteArrayOutputStream, "") {

			@Override
			protected void write() {
				HtmlSectionContainer.this.write(this);
			}
		}.writeXml();
		return byteArrayOutputStream.toString();
	}
}