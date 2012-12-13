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
package org.jbasics.codec;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.coder.Codec;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.xml.transform.XMLTransformerFactory;

public class DOMDocumentCodec implements Codec<Document, String> {
	public static final DOMDocumentCodec SHARED_INSTANCE = new DOMDocumentCodec();

	private final Factory<Transformer> transformerFactory;

	public DOMDocumentCodec() {
		this(null);
	}

	public DOMDocumentCodec(final Factory<Transformer> transformerFactory) {
		this.transformerFactory = transformerFactory != null ? transformerFactory : XMLTransformerFactory.SHARED_INSTANCE;
	}

	@Override
	public Document decode(final String encodedInput) {
		try {
			final DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
			fac.setNamespaceAware(true);
			DocumentBuilder builder;
			builder = fac.newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(encodedInput)));
		} catch (final Exception e) {
			throw DelegatedException.delegate(e);
		}
	}

	@Override
	public String encode(final Document input) {
		try {
			final StringWriter w = new StringWriter();
			this.transformerFactory.newInstance().transform(new DOMSource(input), new StreamResult(w));
			return w.toString();
		} catch (final TransformerException e) {
			throw DelegatedException.delegate(e);
		}
	}

}
