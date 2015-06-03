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

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ArrayConstantsTest {

	@Test
	public void testDiscouragedConstruction() {
		new ArrayConstants(); // To trigger the default creator in order to reach 100% test coverage
	}

	@Test
	public void testZeroByteArray() {
		assertTrue(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY != null);
		assertTrue(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY.length == 0);
	}

	@Test
	public void testZeroShortArray() {
		assertTrue(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY != null);
		assertTrue(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY.length == 0);
	}

	@Test
	public void testZeroIntArray() {
		assertTrue(ArrayConstants.ZERO_LENGTH_INT_ARRAY != null);
		assertTrue(ArrayConstants.ZERO_LENGTH_INT_ARRAY.length == 0);
	}

	@Test
	public void testZeroLongArray() {
		assertTrue(ArrayConstants.ZERO_LENGTH_LONG_ARRAY != null);
		assertTrue(ArrayConstants.ZERO_LENGTH_LONG_ARRAY.length == 0);
	}
}
