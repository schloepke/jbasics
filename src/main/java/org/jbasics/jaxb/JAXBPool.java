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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

public class JAXBPool {
	private final JAXBMarshallerPool marshallerPoolDelegate;
	private final JAXBUnmarshallerPool unmarshallerPoolDelegate;

	public JAXBPool(final Class<?>... classes) {
		final JAXBContextFactory temp = new JAXBContextFactory(ContractCheck.mustNotBeNullOrEmpty(classes, "classes")); //$NON-NLS-1$
		this.marshallerPoolDelegate = new JAXBMarshallerPool(temp);
		this.unmarshallerPoolDelegate = new JAXBUnmarshallerPool(temp);
	}

	public JAXBPool(final String contextPath) {
		this(contextPath, null);
	}

	public JAXBPool(final String contextPath, final Schema schema) {
		JAXBContextFactory temp = new JAXBContextFactory(ContractCheck.mustNotBeNullOrTrimmedEmpty(contextPath, "contextPath")); //$NON-NLS-1$
		this.marshallerPoolDelegate = new JAXBMarshallerPool(temp, schema);
		this.unmarshallerPoolDelegate = new JAXBUnmarshallerPool(temp);
	}

	public JAXBPool(final JAXBContext context) {
		this(context, null);
	}

	public JAXBPool(final JAXBContext context, final Schema schema) {
		this.marshallerPoolDelegate = new JAXBMarshallerPool(ContractCheck.mustNotBeNull(context, "context"), schema); //$NON-NLS-1$
		this.unmarshallerPoolDelegate = new JAXBUnmarshallerPool(context);
	}

	public Marshaller aquireMarshaller() {
		return this.marshallerPoolDelegate.acquire();
	}

	public boolean releaseMarshaller(final Marshaller marshaller) {
		return this.marshallerPoolDelegate.release(marshaller);
	}

	public Unmarshaller aquireUnmarshaller() {
		return this.unmarshallerPoolDelegate.acquire();
	}

	public boolean releaseUnmarshaller(final Unmarshaller unmarshaller) {
		return this.unmarshallerPoolDelegate.release(unmarshaller);
	}
}
