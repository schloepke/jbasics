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

import java.net.URI;

import org.junit.Test;

import org.jbasics.pattern.transpose.Transposer;

public class URITransposerTest {

	public class URNTransposer implements Transposer<URI, URI> {

		public URI transpose(final URI input) {
			String temp = input.getSchemeSpecificPart();
			if (temp.startsWith("jbasics:testOne:")) {
				return URI.create("http://example.com/somebase/somesubse/").resolve(URI.create(temp.substring(16)));
			} else if (temp.startsWith("jbasics:testTwo:")) {
				return URI.create("http://foo.bar/wayelse/inUniverse/found/").resolve(URI.create(temp.substring(16)));
			} else {
				return null;
			}
		}
	}

	@Test
	public void test() {
		URITransposer.SHARED_INSTANCE.setSchemeTransposer("urn", new URNTransposer()); //$NON-NLS-1$
		URI uri = URI.create("http://example.com/somebase/somesubse/nag/nag/0123");
		System.out.println(uri);
		URI result = URITransposer.SHARED_INSTANCE.exchangeBaseURI(uri, URI.create("urn:jbasics:testOne:"), URI.create("urn:jbasics:testTwo:"));
		System.out.println(result);
	}

}
