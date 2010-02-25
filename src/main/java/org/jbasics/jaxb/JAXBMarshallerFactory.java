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
package org.jbasics.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.delegates.LazySoftReferenceDelegate;

import exceptions.DelegatedException;

public class JAXBMarshallerFactory implements Factory<Marshaller> {
	private final Delegate<JAXBContext> jaxbContextDelegate;

	public JAXBMarshallerFactory(final Delegate<JAXBContext> contextDelegate) {
		if (contextDelegate == null) {
			throw new IllegalArgumentException("Null parameter: contextDelegate");
		}
		this.jaxbContextDelegate = contextDelegate;
	}

	public JAXBMarshallerFactory(final Factory<JAXBContext> contextFactory) {
		if (contextFactory == null) {
			throw new IllegalArgumentException("Null parameter: contextFactory");
		}
		this.jaxbContextDelegate = new LazySoftReferenceDelegate<JAXBContext>(contextFactory);
	}

	public Marshaller newInstance() {
		JAXBContext ctx = this.jaxbContextDelegate.delegate();
		if (ctx == null) {
			throw new IllegalStateException("Cannot create unmarshaller since jaxb context delegate returns null");
		}
		try {
			return ctx.createMarshaller();
		} catch (JAXBException e) {
			throw DelegatedException.delegate(e);
		}
	}

}
