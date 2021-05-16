package com.utils.html.sections;

import java.util.ArrayList;
import java.util.List;

import com.utils.xml.stax.XmlStAXWriter;

public class HtmlSectionTable extends HtmlSectionAbstr {

	private String attributeClass;
	private String attributeId;
	private final List<HtmlSection> htmlSectionHeadList;
	private final List<HtmlSection> htmlSectionBodyList;

	public HtmlSectionTable() {

		htmlSectionHeadList = new ArrayList<>();
		htmlSectionBodyList = new ArrayList<>();
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {

		xmlStAXWriter.writeStartElement("table");
		writeAttributes(xmlStAXWriter);

		xmlStAXWriter.writeStartElement("thead");
		for (final HtmlSection htmlSectionHead : htmlSectionHeadList) {
			htmlSectionHead.write(xmlStAXWriter);
		}
		xmlStAXWriter.writeEndElement("thead");

		xmlStAXWriter.writeStartElement("tbody");
		for (final HtmlSection htmlSectionBody : htmlSectionBodyList) {
			htmlSectionBody.write(xmlStAXWriter);
		}
		xmlStAXWriter.writeEndElement("tbody");

		xmlStAXWriter.writeEndElement("table");
	}

	private void writeAttributes(
			final XmlStAXWriter xmlStAXWriter) {

		if (attributeId != null) {
			xmlStAXWriter.writeAttribute("id", attributeId);
		}
		if (attributeClass != null) {
			xmlStAXWriter.writeAttribute("class", attributeClass);
		}
	}

	public HtmlSectionTable addAttributeId(
			final String attributeId) {

		this.attributeId = attributeId;
		return this;
	}

	public HtmlSectionTable addAttributeClass(
			final String attributeClass) {

		this.attributeClass = attributeClass;
		return this;
	}

	public HtmlSectionTable addHtmlSectionHead(
			final HtmlSection htmlSection) {

		htmlSectionHeadList.add(htmlSection);
		return this;
	}

	public HtmlSectionTable addHtmlSectionBody(
			final HtmlSection htmlSection) {

		htmlSectionBodyList.add(htmlSection);
		return this;
	}
}
