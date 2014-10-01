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
package org.jbasics.jaxrs;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Stephan Schl�pke, Christopher Stolle
 */
public class XHTMLStylesheetAttachedXMLProvider implements MessageBodyWriter<XHTMLStylesheetAttachedJAXB<?>> {
	private static final Charset UTF8 = Charset.forName("UTF-8"); //$NON-NLS-1$
	private static final Charset UTF16 = Charset.forName("UTF-16"); //$NON-NLS-1$
	private static final Map<Class<?>, JAXBContext> JAXB_CONTEXT_CACHE = new WeakHashMap<Class<?>, JAXBContext>();

	private final Providers providers;

	public XHTMLStylesheetAttachedXMLProvider(@Context final Providers providers) {
		this.providers = providers;
	}

	private static void writeToXhtml(final XHTMLStylesheetAttachedJAXB<?> t, final Charset charset, final Marshaller marshaller, final Writer w,
			boolean htmlMethod)
			throws JAXBException, IOException {
		try {
			final Transformer transformer = TransformerFactory.newInstance().newTransformer(
					new StreamSource(t.getLocalResource().openStream(), t.getLocalResource().toExternalForm()));
			Source source = new JAXBSource(marshaller, t.getEntity());
			final Result result = new StreamResult(w);
			if (t.isHandleXInclude()) {
				final SAXParserFactory factory = SAXParserFactory.newInstance();
				factory.setNamespaceAware(true);
				factory.setXIncludeAware(true);
				final XMLReader reader = factory.newSAXParser().getXMLReader();
				final ByteArrayOutputStream temp = new ByteArrayOutputStream();
				marshaller.marshal(t.getEntity(), new OutputStreamWriter(temp, charset));
				source = new SAXSource(reader, new InputSource(new ByteArrayInputStream(temp.toByteArray())));
			}
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			if (htmlMethod) {
				ByteArrayOutputStream s = new ByteArrayOutputStream();
				Transformer htmlTransformer = TransformerFactory.newInstance().newTransformer(
						new StreamSource(XHTMLStylesheetAttachedXMLProvider.class.getResourceAsStream("xhtmlToHtmlTransformer.xslt"),
								t.getLocalResource().toExternalForm()));
				transformer.transform(source, new StreamResult(s));
				htmlTransformer.transform(new StreamSource(new ByteArrayInputStream(s.toByteArray())), result);
			} else {
				transformer.transform(source, result);
			}
		} catch (final Exception e) {
			throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
		}
	}

	private static void writeToXml(final XHTMLStylesheetAttachedJAXB<?> t, final Charset charset, final Marshaller marshaller, final Writer wIn)
			throws JAXBException, IOException {
		try {
			final Object jaxbFragmentProperty = marshaller.getProperty(Marshaller.JAXB_FRAGMENT);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			Writer w = wIn;
			ByteArrayOutputStream bos = null;
			if (t.isHandleXInclude()) {
				bos = new ByteArrayOutputStream();
				w = new OutputStreamWriter(bos, charset);
			}
			w.write("<?xml version=\"1.0\" "); //$NON-NLS-1$
			if (charset != XHTMLStylesheetAttachedXMLProvider.UTF8 && charset != XHTMLStylesheetAttachedXMLProvider.UTF16) {
				w.write("encoding=\""); //$NON-NLS-1$
				w.write(charset.name());
				w.write("\" "); //$NON-NLS-1$
			}
			w.write("?>\n"); //$NON-NLS-1$
			w.write("<?xml-stylesheet type=\"text/xsl\" href=\""); //$NON-NLS-1$
			w.write(t.getStylesheet().toASCIIString());
			w.write("\" ?>\n\n"); //$NON-NLS-1$
			marshaller.marshal(t.getEntity(), w);
			if (bos != null) {
				final Transformer transformer = TransformerFactory.newInstance().newTransformer();
				final SAXParserFactory factory = SAXParserFactory.newInstance();
				factory.setNamespaceAware(true);
				factory.setXIncludeAware(true);
				final XMLReader reader = factory.newSAXParser().getXMLReader();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
				transformer.transform(new SAXSource(reader, new InputSource(new ByteArrayInputStream(bos.toByteArray()))), new StreamResult(wIn));
			}
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, jaxbFragmentProperty);
		} catch (final Exception e) {
			throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
		return XHTMLStylesheetAttachedJAXB.class.isAssignableFrom(type) && (MediaType.APPLICATION_XHTML_XML_TYPE.equals(mediaType) ||
				MediaType.APPLICATION_XML_TYPE.equals(mediaType) ||
				MediaType.TEXT_HTML_TYPE.equals(mediaType) ||
				MediaType.TEXT_XML_TYPE.equals(mediaType) ||
				mediaType != null && mediaType.getSubtype().endsWith("+xml")); //$NON-NLS-1$
	}

	@Override
	public long getSize(final XHTMLStylesheetAttachedJAXB<?> t, final Class<?> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(final XHTMLStylesheetAttachedJAXB<?> t, final Class<?> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType,
			final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException, WebApplicationException {
		try {
			final Marshaller marshaller = getMarshaller(t.getEntityType(), mediaType);
			Charset charset = XHTMLStylesheetAttachedXMLProvider.UTF8;
			if (mediaType != null) {
				final String temp = mediaType.getParameters().get("charset"); //$NON-NLS-1$
				if (temp != null) {
					charset = Charset.forName(temp);
				}
			}
			final Writer w = new OutputStreamWriter(entityStream, charset);
			if (MediaType.APPLICATION_XHTML_XML_TYPE.equals(mediaType) || MediaType.TEXT_HTML_TYPE.equals(mediaType)) {
				XHTMLStylesheetAttachedXMLProvider.writeToXhtml(t, charset, marshaller, w, MediaType.TEXT_HTML_TYPE.equals(mediaType));
			} else {
				XHTMLStylesheetAttachedXMLProvider.writeToXml(t, charset, marshaller, w);
			}
		} catch (final JAXBException e) {
			throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
		}
	}

	protected final Marshaller getMarshaller(final Class<?> type, final MediaType mt) throws JAXBException {
		final ContextResolver<Marshaller> marshallerResolver = this.providers.getContextResolver(Marshaller.class, mt);
		Marshaller marshaller = null;
		if (marshallerResolver != null) {
			marshaller = marshallerResolver.getContext(type);
		}
		if (marshaller == null) {
			JAXBContext context = null;
			final ContextResolver<JAXBContext> cr = this.providers.getContextResolver(JAXBContext.class, mt);
			if (cr != null) {
				context = cr.getContext(type);
			}
			if (context == null) {
				synchronized (XHTMLStylesheetAttachedXMLProvider.JAXB_CONTEXT_CACHE) {
					context = XHTMLStylesheetAttachedXMLProvider.JAXB_CONTEXT_CACHE.get(type);
					if (context == null) {
						context = JAXBContext.newInstance(type);
						XHTMLStylesheetAttachedXMLProvider.JAXB_CONTEXT_CACHE.put(type, context);
					}
				}
			}
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		}
		return marshaller;
	}
}
