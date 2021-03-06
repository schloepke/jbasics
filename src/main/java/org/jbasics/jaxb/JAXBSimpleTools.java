/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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
package org.jbasics.jaxb;

import org.jbasics.checker.ContractCheck;
import org.jbasics.enviroment.JVMEnviroment;
import org.jbasics.exception.DelegatedException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

public class JAXBSimpleTools {
	private final JAXBPool pool;

	public JAXBSimpleTools(final String contextPath) {
		this.pool = new JAXBPool(contextPath);
	}

	public JAXBSimpleTools(final String contextPath, final String schemaResource) {
		if (schemaResource != null) {
			try {
				SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				Schema schema = factory.newSchema(JVMEnviroment.getNotNullResource(schemaResource));
				this.pool = new JAXBPool(contextPath, schema);
			} catch (Exception e) {
				throw DelegatedException.delegate(e);
			}
		} else {
			this.pool = new JAXBPool(contextPath);
		}
	}

	public JAXBSimpleTools(final String contextPath, final Schema schema) {
		this.pool = new JAXBPool(contextPath, schema);
	}

	public JAXBSimpleTools(final Class<?>... classes) {
		this.pool = new JAXBPool(classes);
	}

	public static <T> String marshallToString(final JAXBElement<T> element) {
		return new JAXBSimpleTools(element.getDeclaredType()).marshall(element);
	}

	public <T> String marshall(final T element) {
		Marshaller marshaller = this.pool.aquireMarshaller();
		try {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter writer = new StringWriter();
			marshaller.marshal(element, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw DelegatedException.delegate(e);
		} finally {
			this.pool.releaseMarshaller(marshaller);
		}
	}

	public static <T> String marshallToString(final T element) {
		return new JAXBSimpleTools(element.getClass()).marshall(element);
	}

	public static <T> void marshallToFile(final T element, final File file) {
		new JAXBSimpleTools(element.getClass()).marshall(element, file);
	}

	public <T> void marshall(final T element, final File file) {
		Marshaller marshaller = this.pool.aquireMarshaller();
		try {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(element, file);
		} catch (JAXBException e) {
			throw DelegatedException.delegate(e);
		} finally {
			this.pool.releaseMarshaller(marshaller);
		}
	}

	public static <T> T unmarshallFromString(final Class<? extends T> type, final String content) {
		return new JAXBSimpleTools(type).unmarshall(type, content);
	}

	public <T> T unmarshall(final Class<? extends T> type, final String content) {
		Unmarshaller unmarshaller = this.pool.aquireUnmarshaller();
		try {
			return unmarshaller.unmarshal(new StreamSource(new StringReader(content)), type).getValue();
		} catch (JAXBException e) {
			throw DelegatedException.delegate(e);
		} finally {
			this.pool.releaseUnmarshaller(unmarshaller);
		}
	}

	public static <T> T unmarshallFromURL(final Class<? extends T> type, final URL content) {
		return new JAXBSimpleTools(type).unmarshall(type, content);
	}

	public <T> T unmarshall(final Class<? extends T> type, final URL resource) {
		Unmarshaller unmarshaller = this.pool.aquireUnmarshaller();
		try {
			return unmarshaller.unmarshal(new StreamSource(ContractCheck.mustNotBeNull(resource, "resource").openStream()), type).getValue(); //$NON-NLS-1$
		} catch (JAXBException e) {
			throw DelegatedException.delegate(e);
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		} finally {
			this.pool.releaseUnmarshaller(unmarshaller);
		}
	}

	public JAXBPool getPool() {
		return this.pool;
	}

	public <T> T unmarshall(final Class<? extends T> type, final Class<?> resourceBase, final String resourceName) {
		return unmarshall(type, ContractCheck.mustNotBeNull(resourceBase, "resourceBase").getResource( //$NON-NLS-1$
				ContractCheck.mustNotBeNull(resourceName, "resourceName"))); //$NON-NLS-1$
	}
}
