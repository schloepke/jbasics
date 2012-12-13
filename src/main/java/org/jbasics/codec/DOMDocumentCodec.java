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
