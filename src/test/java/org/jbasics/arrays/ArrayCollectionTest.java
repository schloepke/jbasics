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
package org.jbasics.arrays;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ArrayCollectionTest {
	private static final String ONE = "1One";
	private static final String TWO = "2Two";
	private static final String THREE = "3Three";
	private static final String[] ONE_TO_THREE_DATA = new String[]{ArrayCollectionTest.ONE, ArrayCollectionTest.TWO, ArrayCollectionTest.THREE};
	private static final String[] ONE_TO_THREE_DATA_WITH_NULL = new String[]{ArrayCollectionTest.ONE, ArrayCollectionTest.TWO,
			ArrayCollectionTest.THREE, null};
	private static final String[] ONE_TO_THREE_MULTI_DATA = new String[]{ArrayCollectionTest.ONE, ArrayCollectionTest.TWO,
			ArrayCollectionTest.THREE, ArrayCollectionTest.TWO, ArrayCollectionTest.ONE, ArrayCollectionTest.THREE, ArrayCollectionTest.ONE,
			ArrayCollectionTest.TWO};
	private static final String[] ONE_TO_THREE_MULTI_DATA_WITH_NULL = new String[]{ArrayCollectionTest.ONE, ArrayCollectionTest.TWO,
			ArrayCollectionTest.THREE, null, ArrayCollectionTest.TWO, ArrayCollectionTest.ONE, ArrayCollectionTest.THREE, ArrayCollectionTest.ONE,
			ArrayCollectionTest.TWO};
	private static final String FOUR = "4Four";

	@Test
	public void testConstructuion() {
		ArrayCollection<String> constructed = new ArrayCollection<String>(ArrayCollectionTest.ONE_TO_THREE_DATA);
		Assert.assertEquals(3, constructed.size());
		Assert.assertEquals(ArrayCollectionTest.ONE, constructed.get(0));
		Assert.assertEquals(ArrayCollectionTest.TWO, constructed.get(1));
		Assert.assertEquals(ArrayCollectionTest.THREE, constructed.get(2));
		constructed = new ArrayCollection<String>(ArrayCollectionTest.ONE_TO_THREE_DATA, 1, 2);
		Assert.assertEquals(2, constructed.size());
		Assert.assertEquals(ArrayCollectionTest.TWO, constructed.get(0));
		Assert.assertEquals(ArrayCollectionTest.THREE, constructed.get(1));
		Assert.assertFalse(constructed.isEmpty());
		constructed = new ArrayCollection<String>();
		Assert.assertTrue(constructed.isEmpty());
		Assert.assertEquals(0, constructed.size());
	}

	@Test
	public void testAcces() {
		ArrayCollection<String> constructed = new ArrayCollection<String>(ArrayCollectionTest.ONE_TO_THREE_DATA);
		Assert.assertEquals(ArrayCollectionTest.TWO, constructed.get(1));
		Assert.assertEquals(-1, constructed.indexOf(ArrayCollectionTest.FOUR));
		Assert.assertEquals(2, constructed.indexOf(ArrayCollectionTest.THREE));
		Assert.assertEquals(1, constructed.indexOf(new String(ArrayCollectionTest.TWO.getBytes())));
		Assert.assertEquals(-1, constructed.indexOf(null));
		Assert.assertEquals(3, new ArrayCollection<String>(ArrayCollectionTest.ONE_TO_THREE_DATA_WITH_NULL).indexOf(null));
		try {
			constructed.get(3);
			Assert.fail("Access over the limit does not throw an exception");
		} catch (final IndexOutOfBoundsException e) {
			// expected
		}
		try {
			constructed.get(-1);
			Assert.fail("Access under zerodoes not throw an exception");
		} catch (final IndexOutOfBoundsException e) {
			// expected
		}
		constructed = new ArrayCollection<String>(ArrayCollectionTest.ONE_TO_THREE_MULTI_DATA);
		Assert.assertEquals(8, constructed.size());
		Assert.assertEquals(5, constructed.lastIndexOf(ArrayCollectionTest.THREE));
		Assert.assertEquals(-1, constructed.lastIndexOf(ArrayCollectionTest.FOUR));
		Assert.assertEquals(7, constructed.lastIndexOf(new String(ArrayCollectionTest.TWO.getBytes())));
		Assert.assertEquals(-1, constructed.lastIndexOf(null));
		Assert.assertEquals(3, new ArrayCollection<String>(ArrayCollectionTest.ONE_TO_THREE_MULTI_DATA_WITH_NULL).lastIndexOf(null));
	}

	@Test
	public void testModification() {
		final ArrayCollection<String> constructed = new ArrayCollection<String>(ArrayCollectionTest.ONE_TO_THREE_DATA);
		try {
			constructed.clear();
			Assert.fail("Modification CLEAR did not throw usupported operation exception");
		} catch (final UnsupportedOperationException e) {
			// expected
		}
		try {
			constructed.set(0, "DUMMY");
			Assert.fail("Modification SET did not throw usupported operation exception");
		} catch (final UnsupportedOperationException e) {
			// expected
		}
		try {
			constructed.add("DUMMY");
			Assert.fail("Modification ADD did not throw usupported operation exception");
		} catch (final UnsupportedOperationException e) {
			// expected
		}
		try {
			constructed.add(0, "DUMMY");
			Assert.fail("Modification ADD(i, x) did not throw usupported operation exception");
		} catch (final UnsupportedOperationException e) {
			// expected
		}
		try {
			constructed.addAll(Arrays.asList("DUMMY"));
			Assert.fail("Modification ADDALL did not throw usupported operation exception");
		} catch (final UnsupportedOperationException e) {
			// expected
		}
		try {
			constructed.addAll(0, Arrays.asList("DUMMY"));
			Assert.fail("Modification ADDALL(i, X) did not throw usupported operation exception");
		} catch (final UnsupportedOperationException e) {
			// expected
		}
		try {
			constructed.remove(0);
			Assert.fail("Modification REMOVE(i) did not throw usupported operation exception");
		} catch (final UnsupportedOperationException e) {
			// expected
		}
		try {
			constructed.remove("DUMMY");
			Assert.fail("Modification REMOVE(x) did not throw usupported operation exception");
		} catch (final UnsupportedOperationException e) {
			// expected
		}
		try {
			constructed.removeAll(Arrays.asList("DUMMY"));
			Assert.fail("Modification REMOVEALL did not throw usupported operation exception");
		} catch (final UnsupportedOperationException e) {
			// expected
		}
		try {
			constructed.retainAll(Arrays.asList("DUMMY"));
			Assert.fail("Modification RETAINALL did not throw usupported operation exception");
		} catch (final UnsupportedOperationException e) {
			// expected
		}
	}
}
