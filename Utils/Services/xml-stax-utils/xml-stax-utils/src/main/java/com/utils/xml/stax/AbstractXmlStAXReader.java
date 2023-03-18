package com.utils.xml.stax;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Stack;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.utils.io.StreamUtils;
import com.utils.log.Logger;
import com.utils.string.exc.SilentException;

public abstract class AbstractXmlStAXReader implements XmlStAXReader {

	private final InputStream inputStream;
	private final XMLEventReader xmlEventReader;

	protected AbstractXmlStAXReader(
			final InputStream inputStreamParam) {

		InputStream inputStream = null;
		XMLEventReader xmlEventReader = null;
		try {
			inputStream = new BufferedInputStream(inputStreamParam);

			System.setProperty(
					"javax.xml.stream.XMLInputFactory",
					"com.sun.xml.internal.stream.XMLInputFactoryImpl");
			final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
			xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

		} catch (final Exception exc) {
			Logger.printError("failed to create the XML reader");
			Logger.printException(exc);
		}
		this.inputStream = inputStream;
		this.xmlEventReader = xmlEventReader;
	}

	protected AbstractXmlStAXReader(
			final String xmlFilePathString) {

		InputStream inputStream = null;
		XMLEventReader xmlEventReader = null;
		try {
			inputStream = StreamUtils.openInputStream(xmlFilePathString);

			System.setProperty("javax.xml.stream.XMLInputFactory",
					"com.sun.xml.internal.stream.XMLInputFactoryImpl");
			final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
			xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

		} catch (final Exception exc) {
			Logger.printError("failed to create the XML reader");
			Logger.printException(exc);
		}
		this.inputStream = inputStream;
		this.xmlEventReader = xmlEventReader;
	}

	@Override
	public final boolean readXml() {

		boolean success = false;
		if (xmlEventReader != null) {

			try {
				final Stack<String> pathInXml = new Stack<>();
				while (xmlEventReader.hasNext()) {

					final XMLEvent xmlEvent = xmlEventReader.nextEvent();
					if (xmlEvent.isStartElement()) {

						final StartElement startElement = xmlEvent.asStartElement();
						final String localPart = startElement.getName().getLocalPart();
						pathInXml.push(localPart);
						parseXmlEvent(pathInXml, startElement);

					} else if (xmlEvent.isEndElement()) {
						parseXmlEvent(pathInXml, xmlEvent);
						pathInXml.pop();

					} else {
						parseXmlEvent(pathInXml, xmlEvent);
					}
				}
				success = true;

			} catch (final SilentException ignored) {
			} catch (final Exception exc) {
				Logger.printError("failed to read XML file");
				Logger.printException(exc);

			} finally {
				closeStreams();
			}
		}
		return success;
	}

	protected abstract void parseXmlEvent(
			Stack<String> pathInXml,
			XMLEvent xmlEvent) throws SilentException;

	private void closeStreams() {

		try {
			xmlEventReader.close();
			inputStream.close();
		} catch (final Exception ignored) {
		}
	}
}
