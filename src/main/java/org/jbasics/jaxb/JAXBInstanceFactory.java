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

import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.net.URLMappingFactory;
import org.jbasics.pattern.factory.Factory;

public class JAXBInstanceFactory<T> implements Factory<T> {
	private final JAXBUnmarshallerPool pool;
	private final URL documentUrl;

	public JAXBInstanceFactory(final Class<?> documentType, final File documentFile) {
		this(documentType, documentFile.toURI());
	}

	public JAXBInstanceFactory(final Class<?> documentType, final URI documentUri) {
		this(documentType, URLMappingFactory.SHARED_INSTANCE.create(documentUri));
	}

	public JAXBInstanceFactory(final Class<?> documentType, final URL documentUrl) {
		this.pool = new JAXBUnmarshallerPool(ContractCheck.mustNotBeNull(documentType, "documentType")); //$NON-NLS-1$
		this.documentUrl = ContractCheck.mustNotBeNull(documentUrl, "documentUrl"); //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public T newInstance() {
		Unmarshaller um = this.pool.acquire();
		try {
			return (T) um.unmarshal(this.documentUrl);
		} catch (JAXBException e) {
			throw DelegatedException.delegate(e);
		} finally {
			this.pool.release(um);
		}
	}

}
