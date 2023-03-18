package com.utils.html.sections.parents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.utils.html.sections.AbstractHtmlSection;
import com.utils.html.sections.HtmlSection;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class HtmlSectionParent extends AbstractHtmlSection {

	private final String tagName;
	private final Map<String, String> attributeMap;
	private final List<HtmlSection> htmlSectionList;

	protected HtmlSectionParent(
			final String tagName) {

		this.tagName = tagName;
		attributeMap = new HashMap<>();
		htmlSectionList = new ArrayList<>();
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {

		xmlStAXWriter.writeStartElement(tagName);
		writeAttributes(xmlStAXWriter);
		for (final HtmlSection htmlSection : htmlSectionList) {
			htmlSection.write(xmlStAXWriter);
		}
		xmlStAXWriter.writeEndElement(tagName);
	}

	protected void writeAttributes(
			final XmlStAXWriter xmlStAXWriter) {

		for (final Map.Entry<String, String> mapEntry : attributeMap.entrySet()) {

			final String name = mapEntry.getKey();
			final String value = mapEntry.getValue();
			if (value != null) {
				xmlStAXWriter.writeAttribute(name, value);
			}
		}
	}

	public HtmlSectionParent addAttributeId(
			final String attributeId) {
		return addAttribute("id", attributeId);
	}

	public HtmlSectionParent addAttributeClass(
			final String attributeClass) {
		return addAttribute("class", attributeClass);
	}

	public HtmlSectionParent addAttribute(
			final String name,
			final String value) {

		attributeMap.put(name, value);
		return this;
	}

	public HtmlSectionParent addHtmlSection(
			final HtmlSection htmlSection) {

		htmlSectionList.add(htmlSection);
		return this;
	}
}
