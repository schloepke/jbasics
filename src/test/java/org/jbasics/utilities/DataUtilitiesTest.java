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

import junit.framework.Assert;
import org.junit.Test;

/**
 * Test class for {@link DataUtilities}.
 *
 * @author Stephan Schloepke
 */
public class DataUtilitiesTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testCoalesce() {
		new DataUtilities(); // Dummy for 100% test coverage
		Double n1 = Double.valueOf(1.345d);
		Integer n2 = Integer.valueOf(17);
		Float n3 = null;
		String t1 = n1.toString();
		String t2 = n2.toString();
		String t3 = null;
		Assert.assertSame(t1, DataUtilities.coalesce(t3, t1, t2));
		Assert.assertSame(t1, DataUtilities.coalesce(t3, t3, t1));
		Assert.assertSame(t2, DataUtilities.coalesce(t2, t1, t3));
		Assert.assertSame(t2, DataUtilities.coalesce(t3, t2, t1));
		Assert.assertSame(n1, DataUtilities.coalesce(n1, n2, n3));
		Assert.assertSame(n2, DataUtilities.coalesce(n3, n2, n1));
		Assert.assertSame(n1, DataUtilities.coalesce(null, n1));
		Assert.assertSame(null, DataUtilities.coalesce(null, null, n3));
	}
}
