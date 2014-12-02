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

import org.junit.Assert;
import org.jbasics.testing.Java14LoggingTestCase;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

public class TagURIFactoryTest extends Java14LoggingTestCase {

	@Test
	public void testTagURICreate() {
		TagURIFactory test = null;
		test = TagURIFactory.newInstance("mark.mustermann@example.org", 2010, 2); //$NON-NLS-1$
		Assert.assertEquals(URI.create("tag:mark.mustermann@example.org,2010-02"), test.toUri()); //$NON-NLS-1$
		test = TagURIFactory.newInstance("mark.mustermann@example.org", 2010); //$NON-NLS-1$
		Assert.assertEquals(URI.create("tag:mark.mustermann@example.org,2010"), test.toUri()); //$NON-NLS-1$
		test = TagURIFactory.newInstance("mark.mustermann@example.org", 2010, 4, 8); //$NON-NLS-1$
		Assert.assertEquals(URI.create("tag:mark.mustermann@example.org,2010-04-08"), test.toUri()); //$NON-NLS-1$
		test = TagURIFactory.newInstance("mark.mustermann@example.org", 2010, 7, 1); //$NON-NLS-1$
		Assert.assertEquals(URI.create("tag:mark.mustermann@example.org,2010-07"), test.toUri()); //$NON-NLS-1$
		test = TagURIFactory.newInstance("mark.mustermann@example.org", 2010, 1, 1); //$NON-NLS-1$
		Assert.assertEquals(URI.create("tag:mark.mustermann@example.org,2010"), test.toUri()); //$NON-NLS-1$
		test = TagURIFactory.newInstance("mark.mustermann@example.org", 2010, 1, 7); //$NON-NLS-1$
		Assert.assertEquals(URI.create("tag:mark.mustermann@example.org,2010-01-07"), test.toUri()); //$NON-NLS-1$
	}

	@Test
	public void testTagUri() {
		TagURIFactory fac = TagURIFactory.newInstance("mark.mustermann@example.org", 2010, 2);
		Level level = Level.INFO;
		logUri(level, fac.create("web/example"));
	}

	private void logUri(final Level level, final URI input) {
		assert level != null && input != null;
		this.logger.log(level, "URI: {0}", input.toASCIIString());
		this.logger.log(level, " +-- URI-Scheme: {0}", input.getScheme());
		this.logger.log(level, " +-- URI-Authority: {0}", input.getAuthority());
		this.logger.log(level, " +-- URI-Path: {0}", input.getPath());
		this.logger.log(level, " +-- URI-Query: {0}", input.getQuery());
		this.logger.log(level, " +-- URI-Fragment: {0}", input.getFragment());
		this.logger.log(level, " +-- URI-Scheme Specific: {0}", input.getSchemeSpecificPart());
	}

	@Test
	public void testUri() throws URISyntaxException {
		Level level = Level.INFO;
		logUri(level, URI.create("http://example.org/one/two?query=x#fragment"));
		logUri(level, URI.create("ftp://example.org/one/two?query=x#fragment"));
		logUri(level, URI.create("tag://example.org/some:world:is:here?query#fragment"));
		logUri(level, URI.create("resource://example.org/some:world:is:here?query#fragment"));
		logUri(level, URI.create("tag:stephan@schloepke.de,2010:blink/blub/bla?query#fragment"));

		logUri(level, new URI("tag", null, "//stephan@schloepke.de,2010/blink/blub/bla", "query", "fragment"));
	}
}
