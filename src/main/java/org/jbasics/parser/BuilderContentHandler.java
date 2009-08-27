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

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.namespace.QName;

import org.jbasics.parser.invoker.Invoker;
import org.jbasics.types.tuples.Pair;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

@SuppressWarnings("unchecked")
public class BuilderContentHandler<T> extends DefaultHandler {
	private final BuilderParserContext<T> context;
	private final AtomicBoolean parsing;
	private StateStack<BuildHandler> states;
	private T result;
	private StringBuilder characterBuffer;
	private Locator locator;
	private NamespacePrefixStack prefixes;

	private CustomParser activeCustomParser;
	private ContentHandler activeCustomParserContentHandler;
	private int customParserDepth;

	public BuilderContentHandler(BuilderParserContext<T> context) {
		this.context = context;
		this.parsing = new AtomicBoolean(false);
	}

	public T getParsingResult() {
		return this.result;
	}

	@Override
	public void startDocument() throws SAXException {
		if (this.parsing.compareAndSet(false, true)) {
			this.states = new StateStack<BuildHandler>();
			this.characterBuffer = new StringBuilder();
			this.prefixes = new NamespacePrefixStack();
			this.result = null;
		} else {
			throw new IllegalStateException("Start of document event occured while already parsing another document");
		}
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO: setResult. Maybe using a sort of push handler?
		if (!this.parsing.compareAndSet(true, false)) {
			throw new IllegalStateException("End of document event occured while not parsing");
		}
		this.prefixes = null;
		this.characterBuffer = null;
		this.states = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (this.parsing.get()) {
			try {
				if (this.activeCustomParserContentHandler != null) {
					this.activeCustomParserContentHandler.startElement(uri, localName, qName, attributes);
					this.customParserDepth++;
				} else {
					QName name = createQualifiedName(uri, localName, qName);
					ParsingInfo parseInfo = null;
					if (this.states.isEmpty()) {
						// Root processing
						parseInfo = this.context.getParsingInfo(name);
						if (parseInfo == null) {
							throw new SAXException("Unknown root element " + name);
						}
					} else {
						BuildHandler current = this.states.peek();
						if (this.characterBuffer.length() > 0) {
							current.addText(this.characterBuffer.toString());
							this.characterBuffer.setLength(0);
						}
						if (current instanceof CustomParserRegistry) {
							// TODO: We need to handle the attributes here but ignoring this right now
							Map<QName, String> attTemp = Collections.emptyMap();
							CustomParser customParser = ((CustomParserRegistry)current).getCustomParser(name, attTemp);
							if (customParser != null) {
								this.activeCustomParser = customParser;
								this.customParserDepth = 0;
								this.activeCustomParserContentHandler = customParser.beginParsing();
								this.activeCustomParserContentHandler.setDocumentLocator(this.locator);
								this.activeCustomParserContentHandler.startDocument();
								for (Pair<String, URI> prefix : this.prefixes) {
									this.activeCustomParserContentHandler.startPrefixMapping(prefix.left(), prefix.right().toString());
								}
								this.activeCustomParserContentHandler.startElement(uri, localName, qName, attributes);
								// here we need to end execution
								return;
							}
						}
						parseInfo = current.getParsingInfo();
						if (parseInfo != null) {
							Pair<ParsingInfo, Invoker<?, ?>> x = parseInfo.getElementInvoker(name);
							if (x != null) {
								parseInfo = x.first();
							} else {
								parseInfo = null;
							}
						}
					}
					if (parseInfo == null) {
						StringBuilder message = new StringBuilder("Unrecognized element ").append(name);
						if (this.locator != null) {
							message.append(" (").append(this.locator.getLineNumber()).append("/").append(this.locator.getColumnNumber()).append(")");
						}
						throw new SAXParseException(message.toString(), this.locator);
					}
					
					// TODO: Here we need to discover if we have a content handler delegate set so we can delegate everything instead of the following
					
					
					BuildHandler handler = new BuildHandlerImpl(name, parseInfo);
					for (int i = 0; i < attributes.getLength(); i++) {
						QName attrName = createQualifiedName(attributes.getURI(i), attributes.getLocalName(i), attributes.getQName(i));
						String attrValue = attributes.getValue(i);
						handler.setAttribute(attrName, attrValue);
					}
					this.states.push(handler);
				}
			} catch (RuntimeException eo) {
				throw createParsingException(eo);
			}
		} else {
			throw new IllegalStateException("Start of Element event occured while not parsing");
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.parsing.get()) {
			try {
				if (this.activeCustomParserContentHandler != null) {
					this.activeCustomParserContentHandler.endElement(uri, localName, qName);
					if (this.customParserDepth <= 0) {
						this.activeCustomParserContentHandler.endDocument();
						this.activeCustomParser.finishParsing();
						this.activeCustomParserContentHandler = null;
						this.activeCustomParser = null;
					} else {
						this.customParserDepth--;
					}
				} else {
					QName name = createQualifiedName(uri, localName, qName);
					BuildHandler current = this.states.pop();
					if (this.characterBuffer.length() > 0) {
						current.addText(this.characterBuffer.toString());
						this.characterBuffer.setLength(0);
					}
					if (this.states.isEmpty()) {
						this.result = (T) current.getResult();
					} else {
						BuildHandler parent = this.states.peek();
						parent.addElement(name, current.getResult());
					}
				}
			} catch (RuntimeException e) {
				throw createParsingException(e);
			}
		} else {
			throw new IllegalStateException("Start of Element event occured while not parsing");
		}
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		if (this.activeCustomParserContentHandler != null) {
			this.activeCustomParserContentHandler.startPrefixMapping(prefix, uri);
		} else {
			this.prefixes.pushMapping(prefix, URI.create(uri));
		}
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		if (this.activeCustomParserContentHandler != null) {
			this.activeCustomParserContentHandler.endPrefixMapping(prefix);
		} else {
			this.prefixes.popMapping(prefix);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (this.activeCustomParserContentHandler != null) {
			this.activeCustomParserContentHandler.characters(ch, start, length);
		} else {
			this.characterBuffer.append(ch, start, length);
		}
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		if (this.activeCustomParserContentHandler != null) {
			this.activeCustomParserContentHandler.ignorableWhitespace(ch, start, length);
		} else {
			System.out.println("Found ignorable whitespace");
		}
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		super.setDocumentLocator(locator);
		this.locator = locator;
	}

	private SAXParseException createParsingException(RuntimeException eo) {
		Throwable e = eo;
		while (e.getCause() != null && e.getCause() != e) {
			e = e.getCause();
		}
		StringBuilder message = new StringBuilder();
		message.append("[").append(e.getClass().getSimpleName()).append("] ").append(e.getMessage());
		if (this.locator != null) {
			message.append(" (").append(this.locator.getLineNumber()).append("/").append(this.locator.getColumnNumber()).append(")");
		}
		SAXParseException en = new SAXParseException(message.toString(), this.locator);
		en.setStackTrace(e.getStackTrace());
		return en;
	}

	private QName createQualifiedName(String namespace, String localname, String qName) {
		String prefix = null;
		if (qName != null) {
			int temp = qName.indexOf(':');
			if (temp > 0) {
				prefix = qName.substring(0, temp);
			}
		}
		// Some people append a / at the end of the namespace. However this is quite problematic really so we remove it
		// in general.
		// FIXME: We need to check that with the xml spec!!
		if (namespace.endsWith("/")) {
			namespace = namespace.substring(0, namespace.length() -1);
		}
		if (prefix != null) {
			return new QName(namespace, localname, prefix);
		} else {
			return new QName(namespace, localname);
		}
	}
}
