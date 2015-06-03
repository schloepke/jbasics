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
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.delegates.LazySoftReferenceDelegate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;

public class JAXBMarshallerFactory implements Factory<Marshaller> {
	private final Delegate<JAXBContext> jaxbContextDelegate;
	private final Schema schema;

	public JAXBMarshallerFactory(final Delegate<JAXBContext> contextDelegate) {
		this(contextDelegate, null);
	}

	public JAXBMarshallerFactory(final Delegate<JAXBContext> contextDelegate, final Schema schema) {
		this.jaxbContextDelegate = ContractCheck.mustNotBeNull(contextDelegate, "contextDelegate"); //$NON-NLS-1$
		this.schema = schema;
	}

	public JAXBMarshallerFactory(final Factory<JAXBContext> contextFactory) {
		this(contextFactory, null);
	}

	public JAXBMarshallerFactory(final Factory<JAXBContext> contextFactory, final Schema schema) {
		this(new LazySoftReferenceDelegate<JAXBContext>(ContractCheck.mustNotBeNull(contextFactory, "contextFactory")), schema); //$NON-NLS-1$
	}

	public Marshaller newInstance() {
		JAXBContext ctx = this.jaxbContextDelegate.delegate();
		if (ctx == null) {
			throw new IllegalStateException("Cannot create unmarshaller since jaxb context delegate returns null");
		}
		try {
			Marshaller m = ctx.createMarshaller();
			if (this.schema != null) {
				m.setSchema(this.schema);
			}
			return m;
		} catch (JAXBException e) {
			throw DelegatedException.delegate(e);
		}
	}
}
