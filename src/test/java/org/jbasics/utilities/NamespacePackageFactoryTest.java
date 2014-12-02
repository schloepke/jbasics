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
package org.jbasics.utilities;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

@SuppressWarnings({"nls", "static-method"})
public class NamespacePackageFactoryTest {

	@Test
	public void test() {
		Assert.assertEquals("com.step_h.products.jbasics",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("http://www.step-h.com/products/jbasics")));
		Assert.assertEquals("com.step_h.products.jbasics",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("http://step-h.com/products/jbasics")));
		Assert.assertEquals("com.h.step.products.jbasics",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("urn:step-h.com:products:jbasics")));
		Assert.assertEquals("step_h.products.jbasics",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("http://step-h/products/jbasics")));
		Assert.assertEquals("com.step_h.static_.data",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("http://step-h.com/static/data")));
		Assert.assertEquals("com.step_h.long_._123number",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("http://step-h.com/long/123number")));
		Assert.assertEquals("com.step_h.extended",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("http://step-h.com/extended.xls")));
	}
}
