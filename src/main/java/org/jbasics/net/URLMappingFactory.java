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
package org.jbasics.net;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.factory.ParameterFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class URLMappingFactory implements ParameterFactory<URL, URI> {
	public final static URLMappingFactory SHARED_INSTANCE = new URLMappingFactory();
	private final Map<String, ParameterFactory<URL, URI>> schemeFactories;

	public URLMappingFactory() {
		this.schemeFactories = new ConcurrentHashMap<String, ParameterFactory<URL, URI>>();
	}

	public void setSchemeFactory(final String scheme, final ParameterFactory<URL, URI> factory) {
		if (factory == null) {
			this.schemeFactories.remove(ContractCheck.mustNotBeNullOrTrimmedEmpty(scheme, "scheme")); //$NON-NLS-1$
		} else {
			this.schemeFactories.put(ContractCheck.mustNotBeNullOrTrimmedEmpty(scheme, "scheme"), factory); //$NON-NLS-1$
		}
	}

	public URL create(final URI resourceUri) {
		String scheme = ContractCheck.mustNotBeNull(resourceUri, "resourceUri").getScheme(); //$NON-NLS-1$
		ParameterFactory<URL, URI> factory = this.schemeFactories.get(scheme);
		if (factory != null) {
			URL temp = factory.create(resourceUri);
			if (temp != null) {
				return temp;
			}
		}
		try {
			URI newResourceUri = URITransposer.SHARED_INSTANCE.transpose(resourceUri);
			scheme = newResourceUri.getScheme();
			factory = this.schemeFactories.get(scheme);
			if (factory != null) {
				URL temp = factory.create(newResourceUri);
				if (temp != null) {
					return temp;
				}
			}
			return newResourceUri.toURL();
		} catch (MalformedURLException e) {
			throw DelegatedException.delegate(e);
		}
	}
}
