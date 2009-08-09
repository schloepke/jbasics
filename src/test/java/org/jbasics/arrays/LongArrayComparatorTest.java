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
package org.jbasics.arrays;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LongArrayComparatorTest {

	private long[] base;
	private long[] equalToBaseButNotSame;
	private long[] zero;
	private long[] lessThanBase;

	@Before
	public void setUpArrays() {
		this.base = new long[37];
		for (int i = 1; i < this.base.length; i++) {
			this.base[i] = (long) (Math.random() * Long.MAX_VALUE);
		}
		this.base[0] = 13;
		this.equalToBaseButNotSame = new long[this.base.length];
		System.arraycopy(this.base, 0, this.equalToBaseButNotSame, 0, this.base.length);
		this.lessThanBase = new long[this.base.length];
		System.arraycopy(this.base, 0, this.lessThanBase, 0, this.base.length);
		this.lessThanBase[0] = 12; 
		this.zero = new long[23];
		for (int i = 0; i < this.zero.length; i++) {
			this.zero[i] = 0;
		}
	}

	@Test
	public void testCompare() {
		assertEquals(0, LongArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		assertEquals(0, LongArrayComparator.COMPARATOR.compare(null, null));
		assertEquals(0, LongArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		assertEquals(1, LongArrayComparator.COMPARATOR.compare(this.base, null));
		assertEquals(1, LongArrayComparator.COMPARATOR.compare(this.base, this.lessThanBase));
		assertEquals(1, LongArrayComparator.COMPARATOR.compare(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		assertEquals(-1, LongArrayComparator.COMPARATOR.compare(null, this.base));
		assertEquals(-1, LongArrayComparator.COMPARATOR.compare(this.lessThanBase, this.base));
		assertEquals(-1, LongArrayComparator.COMPARATOR.compare(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
	}

	@Test
	public void testCompareArrays() {
		assertEquals(0, LongArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		assertEquals(0, LongArrayComparator.compareArrays(null, null));
		assertEquals(0, LongArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		assertEquals(1, LongArrayComparator.compareArrays(this.base, null));
		assertEquals(1, LongArrayComparator.compareArrays(this.base, this.lessThanBase));
		assertEquals(1, LongArrayComparator.compareArrays(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		assertEquals(-1, LongArrayComparator.compareArrays(null, this.base));
		assertEquals(-1, LongArrayComparator.compareArrays(this.lessThanBase, this.base));
		assertEquals(-1, LongArrayComparator.compareArrays(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		// single element compare
		assertEquals(0, LongArrayComparator.compareArrays(new long[]{10}, new long[]{10}));
		assertEquals(1, LongArrayComparator.compareArrays(new long[]{10}, new long[]{8}));
		assertEquals(-1, LongArrayComparator.compareArrays(new long[]{8}, new long[]{10}));
	}

	@Test
	public void testIsZeroOrNull() {
		assertTrue(LongArrayComparator.isZeroOrNull(null));
		assertTrue(LongArrayComparator.isZeroOrNull(ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		assertTrue(LongArrayComparator.isZeroOrNull(this.zero));
		assertFalse(LongArrayComparator.isZeroOrNull(this.base));
	}

	@Test
	public void testIsGreaterThan() {
		assertTrue(LongArrayComparator.isGreaterThan(this.base, null));
		assertTrue(LongArrayComparator.isGreaterThan(this.base, this.lessThanBase));
		assertTrue(LongArrayComparator.isGreaterThan(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		assertFalse(LongArrayComparator.isGreaterThan(null, this.base));
		assertFalse(LongArrayComparator.isGreaterThan(this.lessThanBase, this.base));
		assertFalse(LongArrayComparator.isGreaterThan(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		assertFalse(LongArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
		assertFalse(LongArrayComparator.isGreaterThan(null, null));
		assertFalse(LongArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsGreaterThanOrEqual() {
		assertTrue(LongArrayComparator.isGreaterThanOrEqual(this.base, null));
		assertTrue(LongArrayComparator.isGreaterThanOrEqual(this.base, this.lessThanBase));
		assertTrue(LongArrayComparator.isGreaterThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		assertFalse(LongArrayComparator.isGreaterThanOrEqual(null, this.base));
		assertFalse(LongArrayComparator.isGreaterThanOrEqual(this.lessThanBase, this.base));
		assertFalse(LongArrayComparator.isGreaterThanOrEqual(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		assertTrue(LongArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
		assertTrue(LongArrayComparator.isGreaterThanOrEqual(null, null));
		assertTrue(LongArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsEqual() {
		assertFalse(LongArrayComparator.isEqual(null, this.base));
		assertFalse(LongArrayComparator.isEqual(this.lessThanBase, this.base));
		assertFalse(LongArrayComparator.isEqual(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		assertFalse(LongArrayComparator.isEqual(this.base, null));
		assertFalse(LongArrayComparator.isEqual(this.base, this.lessThanBase));
		assertFalse(LongArrayComparator.isEqual(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		assertTrue(LongArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
		assertTrue(LongArrayComparator.isEqual(null, null));
		assertTrue(LongArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThan() {
		assertTrue(LongArrayComparator.isLessThan(null, this.base));
		assertTrue(LongArrayComparator.isLessThan(this.lessThanBase, this.base));
		assertTrue(LongArrayComparator.isLessThan(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		assertFalse(LongArrayComparator.isLessThan(this.base, null));
		assertFalse(LongArrayComparator.isLessThan(this.base, this.lessThanBase));
		assertFalse(LongArrayComparator.isLessThan(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		assertFalse(LongArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
		assertFalse(LongArrayComparator.isLessThan(null, null));
		assertFalse(LongArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThanOrEqual() {
		assertTrue(LongArrayComparator.isLessThanOrEqual(null, this.base));
		assertTrue(LongArrayComparator.isLessThanOrEqual(this.lessThanBase, this.base));
		assertTrue(LongArrayComparator.isLessThanOrEqual(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		assertFalse(LongArrayComparator.isLessThanOrEqual(this.base, null));
		assertFalse(LongArrayComparator.isLessThanOrEqual(this.base, this.lessThanBase));
		assertFalse(LongArrayComparator.isLessThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		assertTrue(LongArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
		assertTrue(LongArrayComparator.isLessThanOrEqual(null, null));
		assertTrue(LongArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsNotEqual() {
		assertTrue(LongArrayComparator.isNotEqual(null, this.base));
		assertTrue(LongArrayComparator.isNotEqual(this.lessThanBase, this.base));
		assertTrue(LongArrayComparator.isNotEqual(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		assertTrue(LongArrayComparator.isNotEqual(this.base, null));
		assertTrue(LongArrayComparator.isNotEqual(this.base, this.lessThanBase));
		assertTrue(LongArrayComparator.isNotEqual(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		assertFalse(LongArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
		assertFalse(LongArrayComparator.isNotEqual(null, null));
		assertFalse(LongArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
	}
}
