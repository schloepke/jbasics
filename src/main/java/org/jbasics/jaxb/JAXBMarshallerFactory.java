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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.delegates.LazySoftReferenceDelegate;

public class JAXBMarshallerFactory implements Factory<Marshaller> {
	private final Delegate<JAXBContext> jaxbContextDelegate;
	private final boolean formatted;
	private final Schema schema;

	public JAXBMarshallerFactory(final Delegate<JAXBContext> contextDelegate) {
		this(contextDelegate, false, null);
	}

	public JAXBMarshallerFactory(final Delegate<JAXBContext> contextDelegate, final boolean formatted) {
		this(contextDelegate, formatted, null);
	}

	public JAXBMarshallerFactory(final Delegate<JAXBContext> contextDelegate, final Schema schema) {
		this(contextDelegate, false, schema);
	}

	public JAXBMarshallerFactory(final Delegate<JAXBContext> contextDelegate, final boolean formatted, final Schema schema) {
		this.jaxbContextDelegate = ContractCheck.mustNotBeNull(contextDelegate, "contextDelegate"); //$NON-NLS-1$
		this.formatted = formatted;
		this.schema = schema;
	}

	public JAXBMarshallerFactory(final Factory<JAXBContext> contextFactory) {
		this(contextFactory, false, null);
	}

	public JAXBMarshallerFactory(final Factory<JAXBContext> contextFactory, final boolean formatted) {
		this(contextFactory, formatted, null);
	}

	public JAXBMarshallerFactory(final Factory<JAXBContext> contextFactory, final Schema schema) {
		this(contextFactory, false, schema);
	}

	public JAXBMarshallerFactory(final Factory<JAXBContext> contextFactory, final boolean formatted, final Schema schema) {
		this(new LazySoftReferenceDelegate<JAXBContext>(ContractCheck.mustNotBeNull(contextFactory, "contextFactory")), formatted, schema); //$NON-NLS-1$
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
			if(formatted) {
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			}
			return m;
		} catch (JAXBException e) {
			throw DelegatedException.delegate(e);
		}
	}
}
