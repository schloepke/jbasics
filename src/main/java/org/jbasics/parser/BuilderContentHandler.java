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

import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.namespace.QName;

import org.jbasics.parser.invoker.Invoker;
import org.jbasics.types.tuples.Pair;
import org.xml.sax.Attributes;
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
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (this.parsing.get()) {
			try {
				QName name = new QName(uri, localName);
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
						message.append(" (").append(this.locator.getLineNumber()).append("/").append(
								this.locator.getColumnNumber()).append(")");
					}
					throw new SAXParseException(message.toString(), this.locator);
				}
				BuildHandler handler = new BuildHandlerImpl(name, parseInfo);
				for (int i = 0; i < attributes.getLength(); i++) {
					QName attrName = new QName(attributes.getURI(i), attributes.getLocalName(i));
					String attrValue = attributes.getValue(i);
					handler.setAttribute(attrName, attrValue);
				}
				this.states.push(handler);
				// newBuilderInfo = currentState.getBuilderInstanceFor(temp)
				// newBuilderInfo.processAttributes(attributes)
				// stateStack.push(newBuilderInfo)
				// Ok what do we have to do now.
				//
				// 1. We need to create a new Builder suitable for the given URI and local name
				// 2. We need to call the attributes on the builder (consume them)
				// 3. We need to push the new builder on a stack in order to add the result of the
				// builder to the parent in the end element
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
				QName name = new QName(uri, localName);
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
			} catch (RuntimeException e) {
				throw createParsingException(e);
			}
		} else {
			throw new IllegalStateException("Start of Element event occured while not parsing");
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		this.characterBuffer.append(ch, start, length);
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		System.out.println("Found ignorable whitespace");
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
			message.append(" (").append(this.locator.getLineNumber()).append("/")
					.append(this.locator.getColumnNumber()).append(")");
		}
		SAXParseException en = new SAXParseException(message.toString(), this.locator);
		en.setStackTrace(e.getStackTrace());
		return en;
	}

}
