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
package org.jbasics.xml.types;

import java.net.URI;

import org.jbasics.checker.ContractCheck;
import org.jbasics.xml.XmlSerializable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XmlStylesheetProcessInstruction implements XmlSerializable {
	private final String type;
	private final URI href;

	private final String title;
	private final String media;
	private final String charset;
	private final Boolean alternate;

	public XmlStylesheetProcessInstruction(Object type, URI href) {
		this(type, href, null, null, null, null);
	}

	public XmlStylesheetProcessInstruction(Object type, URI href, String title, String media, String charset, Boolean alternate) {
		this.type = ContractCheck.mustNotBeNull(type, "type").toString();
		this.href = ContractCheck.mustNotBeNull(href, "href");
		this.title = title;
		this.media = media;
		this.charset = charset;
		this.alternate = alternate;
	}

	public String getType() {
		return this.type;
	}

	public URI getHref() {
		return this.href;
	}

	public String getTitle() {
		return this.title;
	}

	public String getMedia() {
		return this.media;
	}

	public String getCharset() {
		return this.charset;
	}

	public Boolean getAlternate() {
		return this.alternate;
	}

	public boolean isAlternate() {
		return Boolean.TRUE.equals(this.alternate);
	}

	public void serialize(ContentHandler handler, AttributesImpl attributes) throws SAXException {
		StringBuilder temp = new StringBuilder();
		temp.append("type=\"").append(this.type).append("\" href=\"").append(this.href).append("\"");
		if (this.title != null) {
			temp.append(" title=\"").append(this.title).append("\"");
		}
		if (this.media != null) {
			temp.append(" media=\"").append(this.media).append("\"");
		}
		if (this.charset != null) {
			temp.append(" charset=\"").append(this.charset).append("\"");
		}
		if (this.alternate != null) {
			temp.append(" alternate=\"").append(this.alternate.booleanValue() ? "yes" : "no").append("\"");
		}
		handler.processingInstruction("xml-stylesheet", temp.toString());
	}

}
