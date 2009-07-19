/*
 * Copyright (c) 2009 Stephan Schloepke and innoQ Deutschland GmbH
 *
 * Stephan Schloepke: http://www.schloepke.de/
 * innoQ Deutschland GmbH: http://www.innoq.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jbasics.parser;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class BuilderContentHandler<T> implements ContentHandler {
	private Map<Class<?>, Class<?>> builderFactoryMap;
	private Locator documentLocator;
	private boolean skipWhitespace = true;

	public void setDocumentLocator(Locator locator) {
		this.documentLocator = locator;
	}

	// Handling of prefix mapping (we might want to store them somehow)

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// Currently left empty
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		// Currently left empty
	}

	// The document section (we need to only allow start document if the content
	// handler is a document handler)

	public void startDocument() throws SAXException {
		// TODO
	}

	public void endDocument() throws SAXException {
		// TODO
	}

	// The main element handling comes here

	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		// TODO
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		// TODO
	}

	// Here we have anything hadling character content

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO
	}

	// We might want to someday allow processing instructions for the builder
	// but for now we really ignore them

	public void processingInstruction(String target, String data)
			throws SAXException {
		// Currently left empty
	}

	// Ok what is the skipped entity thing?

	public void skippedEntity(String name) throws SAXException {
		throw new SAXParseException("Cannot resolve entity " + name,
				this.documentLocator);
	}

}
