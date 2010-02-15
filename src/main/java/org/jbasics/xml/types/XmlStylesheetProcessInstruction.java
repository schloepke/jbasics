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
