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
import org.jbasics.pattern.transpose.Transposer;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class URITransposer implements Transposer<URI, URI> {
	public final static URITransposer SHARED_INSTANCE = new URITransposer();
	private final Map<String, Transposer<URI, URI>> schemeFactories;

	public URITransposer() {
		this.schemeFactories = new ConcurrentHashMap<String, Transposer<URI, URI>>();
		setSchemeTransposer(JavaResourceURLMapper.SCHEME, JavaResourceURLMapper.SHARED_INSTANCE);
	}

	public void setSchemeTransposer(final String scheme, final Transposer<URI, URI> transposer) {
		if (transposer == null) {
			this.schemeFactories.remove(ContractCheck.mustNotBeNullOrTrimmedEmpty(scheme, "scheme")); //$NON-NLS-1$
		} else {
			this.schemeFactories.put(ContractCheck.mustNotBeNullOrTrimmedEmpty(scheme, "scheme"), transposer); //$NON-NLS-1$
		}
	}

	public URI exchangeBaseURI(final URI input, final URI fromBase, final URI toBase) {
		return transpose(ContractCheck.mustNotBeNull(toBase, "toBase")).resolve( //$NON-NLS-1$
				transpose(ContractCheck.mustNotBeNull(fromBase, "fromBase")).relativize(ContractCheck.mustNotBeNull(input, "input"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public URI transpose(final URI input) {
		String scheme = ContractCheck.mustNotBeNull(input, "input").getScheme(); //$NON-NLS-1$
		Transposer<URI, URI> factory = this.schemeFactories.get(scheme);
		URI temp = null;
		if (factory != null) {
			temp = factory.transpose(input);
		}
		return temp == null ? input : temp;
	}
}
