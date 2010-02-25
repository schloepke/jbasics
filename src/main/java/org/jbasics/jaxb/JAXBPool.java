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
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXBPool {
	private final JAXBMarshallerPool marshallerPoolDelegate;
	private final JAXBUnmarshallerPool unmarshallerPoolDelegate;

	public JAXBPool(final Class<?>... classes) {
		if (classes == null || classes.length == 0) {
			throw new IllegalArgumentException("Null or empty parameter: classes");
		}
		final JAXBContextFactory temp = new JAXBContextFactory(classes);
		this.marshallerPoolDelegate = new JAXBMarshallerPool(temp);
		this.unmarshallerPoolDelegate = new JAXBUnmarshallerPool(temp);
	}

	public JAXBPool(final String contextPath) {
		if (contextPath == null) {
			throw new IllegalArgumentException("Null or parameter: contextPath");
		}
		JAXBContextFactory temp = new JAXBContextFactory(contextPath);
		this.marshallerPoolDelegate = new JAXBMarshallerPool(temp);
		this.unmarshallerPoolDelegate = new JAXBUnmarshallerPool(temp);
	}

	public JAXBPool(final JAXBContext context) {
		if (context == null) {
			throw new IllegalArgumentException("Null or parameter: context");
		}
		this.marshallerPoolDelegate = new JAXBMarshallerPool(context);
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
