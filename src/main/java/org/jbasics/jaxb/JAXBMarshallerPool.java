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

import org.jbasics.types.delegates.UnmodifiableDelegate;
import org.jbasics.types.pools.LazyQueuePool;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;

public class JAXBMarshallerPool extends LazyQueuePool<Marshaller> {

	public JAXBMarshallerPool(final Class<?>... classes) {
		super(new JAXBMarshallerFactory(new JAXBContextFactory(classes)));
	}

	public JAXBMarshallerPool(final String contextPath) {
		super(new JAXBMarshallerFactory(new JAXBContextFactory(contextPath)));
	}

	public JAXBMarshallerPool(final String contextPath, final Schema schema) {
		super(new JAXBMarshallerFactory(new JAXBContextFactory(contextPath), schema));
	}

	public JAXBMarshallerPool(final JAXBContext context) {
		super(new JAXBMarshallerFactory(new UnmodifiableDelegate<JAXBContext>(context)));
	}

	public JAXBMarshallerPool(final JAXBContext context, final Schema schema) {
		super(new JAXBMarshallerFactory(new UnmodifiableDelegate<JAXBContext>(context), schema));
	}

	public JAXBMarshallerPool(final JAXBContextFactory contextFactory) {
		super(new JAXBMarshallerFactory(contextFactory));
	}

	public JAXBMarshallerPool(final JAXBContextFactory contextFactory, final Schema schema) {
		super(new JAXBMarshallerFactory(contextFactory, schema));
	}
}
